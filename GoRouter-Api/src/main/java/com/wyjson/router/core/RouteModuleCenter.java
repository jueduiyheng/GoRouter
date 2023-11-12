package com.wyjson.router.core;

import static com.wyjson.router.core.Constants.GOROUTER_SP_CACHE_KEY;
import static com.wyjson.router.core.Constants.GOROUTER_SP_KEY_ROUTE_MODULE_MAP;
import static com.wyjson.router.core.Constants.ROUTE_MODULE_NAME_SUFFIX;
import static com.wyjson.router.core.Constants.ROUTE_MODULE_PACKAGE;

import android.app.Application;
import android.content.Context;

import com.wyjson.router.GoRouter;
import com.wyjson.router.exception.RouterException;
import com.wyjson.router.module.interfaces.IRouteModule;
import com.wyjson.router.utils.ClassUtils;
import com.wyjson.router.utils.PackageUtils;
import com.wyjson.router.utils.TextUtils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RouteModuleCenter {
    private static final String TAG = GoRouter.logger.getDefaultTag() + "_RM";

    private static boolean registerByPlugin;

    /**
     * 获取路由注册模式
     *
     * @return true [GoRouter-Gradle-Plugin] ,false [scan dex file]
     */
    public static boolean isRegisterByPlugin() {
        return registerByPlugin;
    }

    public static synchronized void load(Application application) {
        loadByPlugin();
        if (registerByPlugin) {
            GoRouter.logger.info(TAG, "Loading mode: Load routes by [GoRouter-Gradle-Plugin] plugin.");
        } else {
            GoRouter.logger.info(TAG, "Loading mode: The runtime loads the route by scanning the dex file.");
            loadByDex(application);
        }

        if (Warehouse.routeGroups.size() == 0) {
            GoRouter.logger.error(TAG, "No route files were found, check your configuration please!");
        }

        if (GoRouter.isDebug()) {
            GoRouter.logger.debug(TAG, String.format(Locale.getDefault(),
                    "Route has already been loaded, RouteGroupIndex[%d], ServiceIndex[%d], InterceptorIndex[%d]",
                    Warehouse.routeGroups.size(),
                    Warehouse.services.size(),
                    Warehouse.interceptors.size()
            ));
        }
    }

    /**
     * GoRouter-Gradle-Plugin plugin will generate code inside this method
     * call this method to register all Routes, Interceptors and Services
     */
    private static void loadByPlugin() {
        registerByPlugin = false;
        // auto generate register code by gradle plugin: GoRouter-Gradle-Plugin
        // looks like below:
        // register("class name");
        // register("class name");
    }

    private static void register(String className) {
        register(className, true);
    }

    /**
     * register by class name
     * Sacrificing a bit of efficiency to solve
     * the problem that the main dex file size is too large
     */
    private static void register(String className, boolean isPlugin) {
        if (!TextUtils.isEmpty(className)) {
            try {
                Class<?> clazz = Class.forName(className);
                Object obj = clazz.getConstructor().newInstance();
                if (obj instanceof IRouteModule) {
                    if (isPlugin) {
                        markRegisteredByPlugin();
                    }
                    ((IRouteModule) obj).load();
                } else {
                    GoRouter.logger.error(TAG,
                            "register failed, class name: " + className
                                    + " should implements one of IRouteModule.");
                }
            } catch (RouterException e) {
                throw new RouterException("[register] " + e.getMessage());
            } catch (Exception e) {
                GoRouter.logger.error(TAG, "register class error:" + className, e);
            }
        }
    }

    /**
     * mark already registered by GoRouter-Gradle-Plugin plugin
     */
    private static void markRegisteredByPlugin() {
        if (!registerByPlugin) {
            registerByPlugin = true;
        }
    }

    private static void loadByDex(Application context) {
        try {
            long startTime = System.currentTimeMillis();
            Set<String> routeModuleMap;
            if (GoRouter.isDebug() || PackageUtils.isNewVersion(context, GOROUTER_SP_CACHE_KEY)) {
                GoRouter.logger.info(TAG, "Run with debug mode or new install, rebuild router map.");
                // These class was generated by GoRouter-Compiler.
                routeModuleMap = ClassUtils.getFileNameByPackageName(context, ROUTE_MODULE_PACKAGE, ROUTE_MODULE_NAME_SUFFIX);
                if (!routeModuleMap.isEmpty()) {
                    context.getSharedPreferences(GOROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).edit().putStringSet(GOROUTER_SP_KEY_ROUTE_MODULE_MAP, routeModuleMap).apply();
                }
                PackageUtils.updateVersion(context, GOROUTER_SP_CACHE_KEY);    // Save new version name when router map update finishes.
            } else {
                GoRouter.logger.info(TAG, "Load router map from cache.");
                routeModuleMap = new HashSet<>(context.getSharedPreferences(GOROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).getStringSet(GOROUTER_SP_KEY_ROUTE_MODULE_MAP, new HashSet<String>()));
            }

            GoRouter.logger.info(TAG, "Find the route loading class for " + routeModuleMap.size() + " modules, cost " + (System.currentTimeMillis() - startTime) + "ms.");
            startTime = System.currentTimeMillis();

            for (String className : routeModuleMap) {
                register(className, false);
            }

            GoRouter.logger.info(TAG, "The loading module route is complete, cost " + (System.currentTimeMillis() - startTime) + "ms.");
        } catch (RouterException e) {
            throw new RouterException("[loadByDex] " + e.getMessage());
        } catch (Exception e) {
            throw new RouterException("GoRouter [loadByDex] exception! [" + e.getMessage() + "]");
        }
    }

}