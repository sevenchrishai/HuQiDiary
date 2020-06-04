package com.ylh.huqidiary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.ylh.huqidiary.utils.AppManager;
import org.greenrobot.eventbus.EventBus;

/**
 * @Author: yinlinhai
 * @Date: 2019/5/31
 */
public abstract class BaseActivity extends Activity {

    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getAppManager().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
        }
    }

    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        ButterKnife.bind(this);
    }

    protected void startActivity(Class<?> toClass) {
        Intent intent = new Intent(mContext, toClass);
        startActivity(intent);
    }

    protected void startActivity(Class<?> toClass, Bundle bundle) {
        Intent intent = new Intent(mContext, toClass);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    protected void startActivity(Class<?> toClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(mContext, toClass);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
