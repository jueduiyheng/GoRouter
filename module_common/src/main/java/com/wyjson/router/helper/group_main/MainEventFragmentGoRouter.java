package com.wyjson.router.helper.group_main;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.wyjson.router.GoRouter;
import com.wyjson.router.model.Card;
import java.lang.String;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY GOROUTER.
 * 事件片段
 * {@link com.wyjson.module_main.fragment.EventFragment}
 */
public class MainEventFragmentGoRouter {
    public static String getPath() {
        return "/main/event/fragment";
    }

    public static Card build() {
        return GoRouter.getInstance().build(getPath());
    }

    public static Fragment go(Context context) {
        return (Fragment) build().go(context);
    }
}
