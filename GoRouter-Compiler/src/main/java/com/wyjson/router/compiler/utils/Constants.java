package com.wyjson.router.compiler.utils;

public class Constants {
    // Generate
    public static final String PROJECT = "GoRouter";
    public static final String SEPARATOR = "$$";
    public static final String WARNING_TIPS = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY GOROUTER.";
    public static final String METHOD_NAME_LOAD = "load";
    public static final String METHOD_NAME_LOAD_ROUTE_GROUP = "loadRouteGroup";
    public static final String METHOD_NAME_LOAD_ROUTE_FOR_x_GROUP = "loadRouteFor%sGroup";
    public static final String FIELD_NAME_ROUTE_GROUPS = "routeGroups";

    // System interface
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "androidx.fragment.app.Fragment";

    public static final String PACKAGE_NAME = "com.wyjson.router";
    public static final String MODULE_PACKAGE_NAME = PACKAGE_NAME + ".module";
    public static final String DOCS_PACKAGE_NAME = PACKAGE_NAME + ".docs";
    public static final String GOROUTER_PACKAGE_NAME = PACKAGE_NAME + ".core.GoRouter";
    public static final String I_ROUTE_MODULE_PACKAGE_NAME = PACKAGE_NAME + ".load.IRouteModule";
    public static final String I_ROUTE_MODULE_GROUP_PACKAGE_NAME = PACKAGE_NAME + ".load.IRouteModuleGroup";
    public static final String I_ROUTE_MODULE_GROUP_METHOD_NAME_LOAD = "load";

    // Log
    public static final String PREFIX_OF_LOGGER = PROJECT + "::Compiler ";
    public static final String NO_MODULE_NAME_TIPS = """
            These no module name, at 'build.gradle', like :
            android {
                defaultConfig {
                    ...
                    javaCompileOptions {
                        annotationProcessorOptions {
                            arguments = [GOROUTER_MODULE_NAME: project.getName()]
                        }
                    }
                }
            }
            """;

    // Options of processor
    public static final String KEY_MODULE_NAME = "GOROUTER_MODULE_NAME";
    public static final String KEY_GENERATE_DOC_NAME = "GOROUTER_GENERATE_DOC";
    public static final String VALUE_ENABLE = "enable";

    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE_PACKAGE = LANG + ".Byte";
    public static final String SHORT_PACKAGE = LANG + ".Short";
    public static final String INTEGER_PACKAGE = LANG + ".Integer";
    public static final String LONG_PACKAGE = LANG + ".Long";
    public static final String FLOAT_PACKAGE = LANG + ".Float";
    public static final String DOUBEL_PACKAGE = LANG + ".Double";
    public static final String BOOLEAN_PACKAGE = LANG + ".Boolean";
    public static final String CHAR_PACKAGE = LANG + ".Character";
    public static final String STRING_PACKAGE = LANG + ".String";
    public static final String SERIALIZABLE_PACKAGE = "java.io.Serializable";
    public static final String PARCELABLE_PACKAGE = "android.os.Parcelable";

    public static final String BYTE_PRIMITIVE = "byte";
    public static final String SHORT_PRIMITIVE = "short";
    public static final String INTEGER_PRIMITIVE = "int";
    public static final String LONG_PRIMITIVE = "long";
    public static final String FLOAT_PRIMITIVE = "float";
    public static final String DOUBEL_PRIMITIVE = "double";
    public static final String BOOLEAN_PRIMITIVE = "boolean";
    public static final String CHAR_PRIMITIVE = "char";

}