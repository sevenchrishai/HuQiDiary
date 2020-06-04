package com.ylh.huqidiary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.ylh.huqidiary.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ylh.huqidiary.bean.DiaryBean;
import com.ylh.huqidiary.db.DiaryDatabaseHelper;
import com.ylh.huqidiary.event.StartUpdateDiaryEvent;
import com.ylh.huqidiary.utils.AppManager;
import com.ylh.huqidiary.utils.GetDate;
import com.ylh.huqidiary.utils.StatusBarCompat;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developerHaoz on 2017/5/11.
 */

public class TestActivity extends Activity {

    private static final String TAG = "TestActivity";

    @Bind(R.id.button1)
    Button mButton1;
    @Bind(R.id.button2)
    Button mButton2;
    @Bind(R.id.button3)
    Button mButton3;
    @Bind(R.id.button4)
    Button mButton4;

//    @Bind(R.id.common_iv_back)
//    ImageView mCommonIvBack;
//    @Bind(R.id.common_iv_edit)
//    ImageView mCommonIvEdit;
//    @Bind(R.id.main_iv_circle)
//    ImageView mMainIvCircle;
//    @Bind(R.id.main_tv_date)
//    TextView mMainTvDate;
//    @Bind(R.id.main_tv_content)
//    TextView mMainTvContent;
//    @Bind(R.id.item_ll_control)
//    LinearLayout mItemLlControl;
//
//    @Bind(R.id.main_rv_show_diary)
//    RecyclerView mMainRvShowDiary;
//    @Bind(R.id.main_rl_main)
//    RelativeLayout mMainRlMain;
//    @Bind(R.id.item_first)
//    LinearLayout mItemFirst;
//    @Bind(R.id.main_ll_main)
//    LinearLayout mMainLlMain;
//    private List<DiaryBean> mDiaryBeanList;
//
//    private DiaryDatabaseHelper mHelper;
//
//    private static String IS_WRITE = "true";
//
//    private int mEditPosition = -1;
//
//    /**
//     * 标识今天是否已经写了日记
//     */
//    private boolean isWrite = false;
//    private static TextView mTvTest;
//
//    public static void startActivity(Context context) {
//        Intent intent = new Intent(context, MainActivity.class);
//        context.startActivity(intent);
//    }
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_main);
//    AppManager.getAppManager().addActivity(this);
//    ButterKnife.bind(this);
//    StatusBarCompat.compat(this, Color.parseColor("#161414"));
//    mHelper = new DiaryDatabaseHelper(this, "Diary.db", null, 1);
//    EventBus.getDefault().register(this);
//    SharedPreferencesUtil spHelper = SharedPreferencesUtil.getInstance(this);
//    getDiaryBeanList();
//    initTitle();
//    mMainRvShowDiary.setLayoutManager(new LinearLayoutManager(this));
//    mMainRvShowDiary.setAdapter(new DiaryAdapter(this, mDiaryBeanList));
//    mTvTest = new TextView(this);
//    mTvTest.setText("hello world");
//}
//
//    private void initTitle() {
//        mMainTvDate.setText("今天，" + GetDate.getDate());
//        mCommonIvBack.setVisibility(View.GONE);
//
//    }
//
//    private List<DiaryBean> getDiaryBeanList() {
//
//        mDiaryBeanList = new ArrayList<>();
//        List<DiaryBean> diaryList = new ArrayList<>();
//        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                String date = cursor.getString(cursor.getColumnIndex("date"));
//                String dateSystem = GetDate.getDate().toString();
//                if (date.equals(dateSystem)) {
//                    mMainLlMain.removeView(mItemFirst);
//                    break;
//                }
//            } while (cursor.moveToNext());
//        }
//
//
//        if (cursor.moveToFirst()) {
//            do {
//                String date = cursor.getString(cursor.getColumnIndex("date"));
//                String title = cursor.getString(cursor.getColumnIndex("title"));
//                String content = cursor.getString(cursor.getColumnIndex("content"));
//                String tag = cursor.getString(cursor.getColumnIndex("tag"));
//                mDiaryBeanList.add(new DiaryBean(date, title, content, tag));
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//
//        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
//            diaryList.add(mDiaryBeanList.get(i));
//        }
//
//        mDiaryBeanList = diaryList;
//        return mDiaryBeanList;
//    }
//
//    @Subscribe
//    public void startUpdateDiaryActivity(StartUpdateDiaryEvent event) {
//        String title = mDiaryBeanList.get(event.getPosition()).getTitle();
//        String content = mDiaryBeanList.get(event.getPosition()).getContent();
//        String tag = mDiaryBeanList.get(event.getPosition()).getTag();
//        UpdateDiaryActivity.startActivity(this, title, content, tag);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @OnClick({R.id.common_iv_edit,R.id.common_iv_diary})
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.common_iv_edit:
//                AddDiaryActivity.startActivity(this);
//                break;
//            case R.id.common_iv_diary:
//                CalendarActivity.startActivity(this);
//                break;
//        }
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        AppManager.getAppManager().AppExit(this);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                Log.d(TAG, "onViewClicked: You Click button1");
                break;
            case R.id.button2:
                Log.d(TAG, "onViewClicked: You Click button2");
                break;
            case R.id.button3:
                Log.d(TAG, "onViewClicked: You Click button3");
                break;
        }
    }
}
