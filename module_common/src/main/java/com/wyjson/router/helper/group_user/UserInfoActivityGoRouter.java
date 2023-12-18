package com.wyjson.router.helper.group_user;

import android.content.Context;
import com.wyjson.router.GoRouter;
import com.wyjson.router.model.Card;
import java.lang.String;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY GOROUTER.
 * 用户信息页面
 * {@link com.wyjson.module_user.activity.UserInfoActivity}
 */
public class UserInfoActivityGoRouter {
    public static String getPath() {
        return "/user/info/activity";
    }

    public static Card build() {
        return GoRouter.getInstance().build(getPath());
    }

    public static void go(Context context) {
        build().go(context);
    }
}
