package com.wyjson.module_main.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.wyjson.module_common.model.TestModel;
import com.wyjson.module_common.route.KotlinRoute;
import com.wyjson.module_common.route.MainRoute;
import com.wyjson.module_common.route.UserRoute;
import com.wyjson.module_common.route.service.user.PayService;
import com.wyjson.module_common.route.service.user.UserService;
import com.wyjson.module_common.utils.ToastUtils;
import com.wyjson.module_main.R;
import com.wyjson.module_main.databinding.MainActivityMainBinding;
import com.wyjson.router.GoRouter;
import com.wyjson.router.annotation.Route;
import com.wyjson.router.callback.GoCallback;
import com.wyjson.router.model.Card;

@Route(path = MainRoute.MainActivity, remark = "主页")
public class MainActivity extends FragmentActivity {

    MainActivityMainBinding vb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = MainActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        showRouteLoadMode();
        registerEvent();
    }

    public void onClickSignInActivity(View view) {
        GoRouter.getInstance().build(UserRoute.SignInActivity).go(this);
    }

    public void onClickParamActivity(View view) {
        GoRouter.getInstance().build(UserRoute.ParamActivity)
                .withInt("age", 78)
                .withString("nickname", "Wyjson")
                .withInt("base", 7758)// 父类里定义的参数
                /**
                 * 使用此方式传递自定义参数需要实现Json服务
                 * Demo示例 {@link com.wyjson.module_common.route.service.JsonServiceImpl}
                 */
                .withObject("test", new TestModel(123, "Jack"))
                .go(this);
    }

    public void onClickCardFragment(View view) {
        Fragment cardFragment = (Fragment) GoRouter.getInstance().build(UserRoute.CardFragment).go(this);
        if (cardFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, cardFragment)
                    .commit();
        }
    }

    public void onClickParamFragment(View view) {
        Fragment cardFragment = (Fragment) GoRouter.getInstance().build(UserRoute.ParamFragment)
                .withInt("age", 78)
                .withString("name", "Wyjson")
                .go(this);
        if (cardFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, cardFragment)
                    .commit();
        }
    }

    public void onClickUserInfoActivity(View view) {
        GoRouter.getInstance().build(UserRoute.UserInfoActivity).go(this, new GoCallback() {
            @Override
            public void onFound(Card card) {

            }

            @Override
            public void onLost(Card card) {

            }

            @Override
            public void onArrival(Card card) {

            }

            @Override
            public void onInterrupt(Card card, @NonNull Throwable exception) {
                if (!TextUtils.isEmpty(exception.getMessage())) {
                    ToastUtils.makeText(MainActivity.this, "onInterrupt:" + exception.getMessage());
                }
            }
        });
    }

    public void onClickUserService(View view) {
        UserService userService = GoRouter.getInstance().getService(UserService.class);
        if (userService != null) {
            ToastUtils.makeText(MainActivity.this, "userId:" + userService.getUserId());
        }
    }

    public void onClickPayService1(View view) {
        PayService alipayService = GoRouter.getInstance().getService(PayService.class, "Alipay");
        if (alipayService != null) {
            ToastUtils.makeText(MainActivity.this, "payType:" + alipayService.getPayType());
        }
    }

    public void onClickPayService2(View view) {
        PayService wechatPayService = GoRouter.getInstance().getService(PayService.class, "WechatPay");
        if (wechatPayService != null) {
            ToastUtils.makeText(MainActivity.this, "payType:" + wechatPayService.getPayType());
        }
    }

    public void onClickEventActivity(View view) {
        GoRouter.getInstance().build(MainRoute.EventActivity).go(this);
    }

    public void onClickKotlinActivity(View view) {
        GoRouter.getInstance().build(KotlinRoute.KotlinActivity)
                .withInt("age", 78)
                .withString("nickname", "Wyjson")
                .go(this);
    }

    private void showRouteLoadMode() {
        if (GoRouter.getInstance().isRouteRegisterMode()) {
            vb.tvLoadMode.setText("路由注册模式:GoRouter-Gradle-Plugin(在打包时注册,节省运行时间)");
            vb.tvLoadMode.setTextColor(Color.parseColor("#0000ff"));
        } else {
            vb.tvLoadMode.setText("路由注册模式:scan dex file(在运行时注册,节省打包时间)");
            vb.tvLoadMode.setTextColor(Color.parseColor("#ff0000"));
        }
    }

    private void registerEvent() {
        // 订阅一下事件
        // 订阅int类型事件(页面处于活跃状态下才会收到)
        GoRouter.getInstance().registerEvent(this, Integer.class, data -> {
            ToastUtils.makeText(MainActivity.this, "MainActivity->String data:" + data);
        });
    }

}