package com.wyjson.router.core;

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

public class RouteModuleLoadCenter {

    private static boolean registerByPlugin;

    /**
     * 获取路由注册模式
     *
     * @return true [GoRouter-Gradle-Plugin] ,false [scan dex file]
     */
    public static boolean isRegisterByPlugin() {
        return registerByPlugin;
    }

    public static synchronized void loadModuleRoute(Application application) {
        loadModuleRouteByPlugin();
        if (registerByPlugin) {
            GoRouter.logger.info(null, "Loading mode: Load routes by [GoRouter-Gradle-Plugin] plugin.");
        } else {
            GoRouter.logger.info(null, "Loading mode: The runtime loads the route by scanning the dex file.");
            loadModuleRouteByDex(application);
        }

        if (Warehouse.routeGroups.size() == 0) {
            GoRouter.logger.error(null, "No mapping files were found, check your configuration please!");
        }

        if (GoRouter.isDebug()) {
            GoRouter.logger.debug(null, String.format(Locale.getDefault(),
                    "GoRouter has already been loaded, RouteGroupIndex[%d], ServiceIndex[%d], InterceptorIndex[%d]",
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
    private static void loadModuleRouteByPlugin() {
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
                    GoRouter.logger.error(null,
                            "register failed, class name: " + className
                                    + " should implements one of IRouteModule.");
                }
            } catch (RouterException e) {
                throw new RouterException("[register] " + e.getMessage());
            } catch (Exception e) {
                GoRouter.logger.error(null, "register class error:" + className, e);
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

    public static final String GOROUTER_SP_CACHE_KEY = "SP_GOROUTER_CACHE";
    public static final String GOROUTER_SP_KEY_MAP = "ROUTE_MODULE_MAP";
    public static final String SEPARATOR = "$$";
    // 路由注册生成类所在包名
    public static final String ROUTE_MODULE_PACKAGE = "com.wyjson.router.module";
    // 路由注册生成类名后缀$$GoRouter
    public static final String MODULE_ROUTE_NAME_SUFFIX = SEPARATOR + "GoRouter";

    private static void loadModuleRouteByDex(Application context) {
        try {
            long startTime = System.currentTimeMillis();
            Set<String> routeModuleMap;
            if (GoRouter.isDebug() || PackageUtils.isNewVersion(context)) {
                GoRouter.logger.info(null, "Run with debug mode or new install, rebuild router map.");
                // These class was generated by GoRouter-Compiler.
                routeModuleMap = ClassUtils.getFileNameByPackageName(context, ROUTE_MODULE_PACKAGE, MODULE_ROUTE_NAME_SUFFIX);
                if (!routeModuleMap.isEmpty()) {
                    context.getSharedPreferences(GOROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).edit().putStringSet(GOROUTER_SP_KEY_MAP, routeModuleMap).apply();
                }
                PackageUtils.updateVersion(context);    // Save new version name when router map update finishes.
            } else {
                GoRouter.logger.info(null, "Load router map from cache.");
                routeModuleMap = new HashSet<>(context.getSharedPreferences(GOROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).getStringSet(GOROUTER_SP_KEY_MAP, new HashSet<String>()));
            }

            GoRouter.logger.info(null, "Find the route loading class for " + routeModuleMap.size() + " modules, cost " + (System.currentTimeMillis() - startTime) + "ms.");
            startTime = System.currentTimeMillis();

            for (String className : routeModuleMap) {
                register(className, false);
            }

            GoRouter.logger.info(null, "The loading module route is complete, cost " + (System.currentTimeMillis() - startTime) + "ms.");
        } catch (RouterException e) {
            throw new RouterException("[loadModuleRouteByDex] " + e.getMessage());
        } catch (Exception e) {
            throw new RouterException("GoRouter [loadModuleRouteByDex] exception! [" + e.getMessage() + "]");
        }
    }

}
