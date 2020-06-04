package com.ylh.huqidiary.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.*;

import com.ylh.huqidiary.R;
import com.ylh.huqidiary.ui.fragments.BaseFragment;
import com.ylh.huqidiary.ui.fragments.DiaryFragment;
import com.ylh.huqidiary.ui.fragments.MineFragment;
import com.ylh.huqidiary.utils.AppManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_search)
    ImageView mCommonIvSearch;
    @Bind({R.id.common_tv_complete})
    TextView mCommonTvComplete;

    @Bind({R.id.rg_main})
    RadioGroup mRg_main;
    @Bind(R.id.tv_add_diary)
    TextView mAddDiary;

    private List<BaseFragment> mBaseFragment;
    /**
     * 选中的Fragment的对应的位置
     */
    private int position;
    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initTitle();
        //初始化Fragment
        initFragment();
        //设置RadioGroup的监听
        setListener();
    }

    private void setListener() {
        mRg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选中框架页面
        mRg_main.check(R.id.rb_diary);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_diary:
                    position = 0;
                    break;
                case R.id.rb_mine:
                    position = 1;
                    break;
                default: //默认第一个(框架)
                    position = 0;
                    break;
            }

            //根据位置得到对应的Fragment
            BaseFragment to = getFragment();
            //替换到Fragment
            switchFrament(mContent, to);
        }
    }

    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from, Fragment to) {
        if (from != to) { //才切换
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); //开启事务
            //判断to有没有被添加
            if (!to.isAdded()) {//to没有被添加
                //1.from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //2.添加to
                if (to != null) {
                    ft.add(R.id.fl_content, to).commit();
                }
            } else { //to已经被添加
                //1.from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //2.显示to
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }
    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragment() {
        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new DiaryFragment());
        mBaseFragment.add(new MineFragment());
    }

    private void initTitle() {
        mCommonIvBack.setVisibility(View.GONE);
        mCommonTvComplete.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({ R.id.common_iv_diary,R.id.tv_add_diary,R.id.common_iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_diary:
//                CalendarActivity.startActivity(this);
                break;
            case R.id.tv_add_diary:
                AddDiaryActivity.startActivity(this);
                break;
            case R.id.common_iv_search:
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}