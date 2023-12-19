package com.wyjson.router.helper.module_user.group_user;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.wyjson.router.GoRouter;
import com.wyjson.router.model.Card;
import com.wyjson.router.model.CardMeta;
import java.lang.String;

/**
 * DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY GOROUTER.
 * 卡片片段
 * {@link com.wyjson.module_user.fragment.CardFragment}
 */
public class UserCardFragmentGoRouter {
    public static String getPath() {
        return "/user/card/fragment";
    }

    public static CardMeta getCardMeta() {
        return GoRouter.getInstance().build(getPath()).getCardMeta();
    }

    public static Card build() {
        return GoRouter.getInstance().build(getPath());
    }

    public static Fragment go(Context context) {
        return (Fragment) build().go(context);
    }
}