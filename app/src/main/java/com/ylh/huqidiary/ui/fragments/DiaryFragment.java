package com.ylh.huqidiary.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.Bind;
import com.ylh.huqidiary.R;
import com.ylh.huqidiary.bean.DiaryBean;
import com.ylh.huqidiary.db.DiaryDatabaseHelper;
import com.ylh.huqidiary.adapter.DiaryAdapter;
import com.ylh.huqidiary.event.MessageEvent;
import com.ylh.huqidiary.utils.GetDate;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/1
 */
public class DiaryFragment extends BaseFragment {

    @Bind(R.id.main_rv_show_diary)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_year)
    TextView mTvYear;

    private static final String TAG = DiaryFragment.class.getSimpleName();
    private List<DiaryBean> mDiaryBeanList;
    private DiaryDatabaseHelper mHelper;
    private DiaryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_diary;
    }

    @Override
    protected void initViews() {
        mHelper = new DiaryDatabaseHelper(mContext, "Diary.db", null, 1);

        getDiaryBeanList();
        mTvYear.setText(GetDate.getDate2().toString().split("-")[0] + "年");
        mAdapter = new DiaryAdapter(mContext, mDiaryBeanList);
        mAdapter.setOnDelListener(new DiaryAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < mDiaryBeanList.size()) {
                    String tag = mDiaryBeanList.get(pos).getTag();
                    SQLiteDatabase dbDelete = mHelper.getWritableDatabase();
                    dbDelete.delete("Diary", "tag = ?", new String[]{tag});
                    mDiaryBeanList.remove(pos);
                    mAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new
//                LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setLayoutManager(mLayoutManager = new GridLayoutManager(mContext, 1));
    }

    @Override
    protected void initData() {
        super.initData();
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
                mDiaryBeanList.add(new DiaryBean(date, date2,time,week,weather,content, tag));
            } while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }

        mDiaryBeanList = diaryList;
        return mDiaryBeanList;
    }

}
