package com.ylh.huqidiary.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import org.greenrobot.eventbus.EventBus;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/1
 */
public abstract class BaseFragment extends Fragment {

    /**
     * 上下文
     */
    protected Context mContext;
    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(attachLayoutRes(), container, false);
        ButterKnife.bind(this, mView);
        initViews();
        return mView;
    }


    protected abstract int attachLayoutRes();

    protected abstract void initViews();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
    }

    /**
     * 当孩子需要初始化数据，或者联网请求绑定数据，展示数据的 等等可以重写该方法
     */
    protected void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
