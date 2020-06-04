package com.ylh.huqidiary.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;
import com.ylh.huqidiary.R;
import com.ylh.huqidiary.adapter.DiaryAdapter;
import com.ylh.huqidiary.bean.DiaryBean;
import com.ylh.huqidiary.db.DiaryDatabaseHelper;
import com.ylh.huqidiary.utils.GetDate;
import com.ylh.huqidiary.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/14
 */
public class SearchActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.recyclerView_search)
    RecyclerView mRecyclerViewSearch;
    private DiaryDatabaseHelper mHelper;
    private DiaryAdapter mAdapter;
    private List<DiaryBean> mDiaryBeanList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //防止软键盘把布局顶开
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ScreenUtils.setStatusBarLightMode(this, false);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerViewSearch.setLayoutManager(new
                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString();
                List<DiaryBean> mList = searchNotes(key);
                mAdapter.updateList(mList);
            }
        });
    }

    private void initData() {
        mHelper = new DiaryDatabaseHelper(mContext, "Diary.db", null, 1);
        getDiaryBeanList();
        mAdapter = new DiaryAdapter(mContext, mDiaryBeanList);
        mRecyclerViewSearch.setAdapter(mAdapter);
    }


    @OnClick({R.id.iv_back,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                startActivity(MainActivity.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(MainActivity.class);
    }

    private List<DiaryBean> getDiaryBeanList() {
        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Diary", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String dateSystem = GetDate.getDate().toString();
                if (date.equals(dateSystem)) {
                    break;
                }
            } while (cursor.moveToNext());
        }
        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String date2 = cursor.getString(cursor.getColumnIndex("date2"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String week = cursor.getString(cursor.getColumnIndex("week"));
                String weather = cursor.getString(cursor.getColumnIndex("weather"));
                String tag = cursor.getString(cursor.getColumnIndex("tag"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                mDiaryBeanList.add(new DiaryBean(date, date2, time, week, weather, content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

    //以关键字查找日记为例，主要是Sql语句
    public ArrayList<DiaryBean> searchNotes(String keywords) {
        ArrayList<DiaryBean> dList = new ArrayList<DiaryBean>();
        if (keywords != null) {
            SQLiteDatabase mDb = mHelper.getWritableDatabase();
            //查询日记标题或内容含有关键字的记录
            Cursor cusror = mDb.query(true, "Diary", new String[]{"date",
                            "date2", "time", "week", "weather", "tag", "content",},
                    "content like '%" + keywords + "%'", null, null, null, null, null);
            while (cusror.moveToNext()) {
                DiaryBean bean = new DiaryBean();
                bean.setDate(cusror.getString(cusror.getColumnIndex("date")));
                bean.setDate2(cusror.getString(cusror.getColumnIndex("date2")));
                bean.setTime(cusror.getString(cusror.getColumnIndex("time")));
                bean.setWeek(cusror.getString(cusror.getColumnIndex("week")));
                bean.setWeather(cusror.getString(cusror.getColumnIndex("weather")));
                bean.setTag(cusror.getString(cusror.getColumnIndex("tag")));
                bean.setContent(cusror.getString(cusror.getColumnIndex("content")));
                dList.add(bean);
            }
            cusror.close();
        }
        return dList;
    }

}