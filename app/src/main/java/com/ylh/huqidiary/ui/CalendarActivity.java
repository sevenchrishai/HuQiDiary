package com.ylh.huqidiary.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.ylh.huqidiary.R;
import com.ylh.huqidiary.utils.AppManager;

public class CalendarActivity extends Activity {

    @Bind(R.id.common_iv_back)
    ImageView mCommonIvBack;
    @Bind(R.id.common_iv_search)
    ImageView mCommonIvSearch;
    @Bind(R.id.common_iv_girl)
    ImageView mCommonIvGirl;
    @Bind({R.id.common_tv_complete})
    TextView mCommonTvComplete;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CalendarActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mCommonIvSearch.setVisibility(View.GONE);
        mCommonTvComplete.setVisibility(View.GONE);
        mCommonIvGirl.setVisibility(View.GONE);
    }

    @OnClick({R.id.common_iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_iv_back:
                MainActivity.startActivity(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.startActivity(this);
    }
}
