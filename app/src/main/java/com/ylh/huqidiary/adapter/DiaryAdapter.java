package com.ylh.huqidiary.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylh.huqidiary.R;
import com.ylh.huqidiary.bean.DiaryBean;
import com.ylh.huqidiary.db.DiaryDatabaseHelper;
import com.ylh.huqidiary.event.MessageEvent;
import com.ylh.huqidiary.event.StartUpdateDiaryEvent;
import com.ylh.huqidiary.ui.AddDiaryActivity;
import com.ylh.huqidiary.ui.MainActivity;
import com.ylh.huqidiary.ui.UpdateDiaryActivity;
import com.ylh.huqidiary.utils.GetDate;

import com.ylh.huqidiary.utils.StringUtil;
import com.ylh.huqidiary.widget.MyDialog;
import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryBean> mDiaryBeanList;
    private MyDialog.Builder dialog = null;

    public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList) {
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.item_rv_diary, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {
        String dateSystem = GetDate.getDate2().toString();
        String date = mDiaryBeanList.get(position).getDate2();   //2019-6-10
        if (date.equals(dateSystem)) {
//            holder.mIvToday.setVisibility(View.VISIBLE);
        }
        String[] _arr = date.split("-");
        holder.mTvMonth.setText(_arr[1] + "月");
        holder.mTvDay.setText(_arr[2]);
        holder.mTvTime.setText(mDiaryBeanList.get(position).getTime());
        // content图文特殊
        String content = mDiaryBeanList.get(position).getContent();
        Set<String> stringSet = StringUtil.getStrContainData(content, "[local]", "[/local]", true);
        for (String key : stringSet) {
            content = StringUtil.trimStr(content, "[local]" + key + "[/local]");
        }
        holder.mTvContent.setText(content);

        holder.mItemLlControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("diaryObj", mDiaryBeanList.get(position));
                AddDiaryActivity.startActivity(mContext, bundle);
            }
        });
        holder.mItemLlControl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                dialog = MyDialog.Builder(mContext);
                dialog.setTitle("提示")
                        .setMessage("确定要删除该日记吗？")
                        .setOnConfirmClickListener("确定", new MyDialog.onConfirmClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (null != mOnSwipeListener) {
                                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                                }
                            }
                        })
                        .setOnCancelClickListener("取消", new MyDialog.onCancelClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        })
                        .build()
                        .showon();
                return false;
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    public void updateList(List<DiaryBean> newlist) {
        this.mDiaryBeanList = newlist;
        notifyDataSetChanged();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder {

        TextView mTvMonth;
        TextView mTvDay;
        TextView mTvTime;
        TextView mTvContent;
        LinearLayout mItemLlControl;
        ImageView mIvToday;
        Button btnDelete;

        DiaryViewHolder(View view) {
            super(view);
            mTvMonth = (TextView) view.findViewById(R.id.main_tv_month);
            mTvDay = (TextView) view.findViewById(R.id.main_tv_day);
            mTvTime = (TextView) view.findViewById(R.id.main_tv_time);
            mTvContent = (TextView) view.findViewById(R.id.main_tv_content);
            mItemLlControl = (LinearLayout) view.findViewById(R.id.item_ll);
            mIvToday = (ImageView) view.findViewById(R.id.iv_today);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}