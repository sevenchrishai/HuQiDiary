package com.ylh.huqidiary.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ylh.huqidiary.R;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/14
 */
public class MyDialog extends Dialog {
    private final String TITLE;
    private final String MESSAGE;
    private final String CONFIRMTEXT;
    private final String CANCELTEXT;
    private final onConfirmClickListener ONCONFIRMCLICKLISTENER;
    private final onCancelClickListener ONCANCELCLICKLISTENER;

    public interface onConfirmClickListener {
        void onClick(View view);
    }

    public interface onCancelClickListener {
        void onClick(View view);
    }

    private MyDialog(@NonNull Context context, String title, String message, String confirmText, String cancelText,
                     onConfirmClickListener onConfirmClickListener, onCancelClickListener onCancelClickListener) {
        super(context, R.style.MyUsualDialog);
        this.TITLE = title;
        this.MESSAGE = message;
        this.CONFIRMTEXT = confirmText;
        this.CANCELTEXT = cancelText;
        this.ONCONFIRMCLICKLISTENER = onConfirmClickListener;
        this.ONCANCELCLICKLISTENER = onCancelClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    private void initView() {
        Button btnConfirm = (Button) findViewById(R.id.btn_confirm);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) findViewById(R.id.tv_message);

        if (!TextUtils.isEmpty(TITLE)) {
            tvTitle.setText(TITLE);
        }
        if (!TextUtils.isEmpty(MESSAGE)) {
            tvMessage.setText(MESSAGE);
        }
        if (!TextUtils.isEmpty(CONFIRMTEXT)) {
            btnConfirm.setText(CONFIRMTEXT);
        }
        if (!TextUtils.isEmpty(CANCELTEXT)) {
            btnCancel.setText(CANCELTEXT);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ONCONFIRMCLICKLISTENER == null) {
                    throw new NullPointerException("clicklistener is not null");
                } else {
                    ONCONFIRMCLICKLISTENER.onClick(view);
                }
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ONCANCELCLICKLISTENER == null) {
                    throw new NullPointerException("clicklistener is not null");
                } else {
                    ONCANCELCLICKLISTENER.onClick(view);
                }
                dismiss();
            }
        });
    }

    public MyDialog showon() {
        show();
        return this;
    }

    public static class Builder {
        private String mTitle;
        private String mMessage;
        private String mConfirmText;
        private String mCancelText;
        private onConfirmClickListener mOnConfirmClickListener;
        private onCancelClickListener mOnCcancelClickListener;
        private Context mContext;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setOnConfirmClickListener(String confirmText, onConfirmClickListener confirmclickListener) {
            this.mConfirmText = confirmText;
            this.mOnConfirmClickListener = confirmclickListener;
            return this;
        }

        public Builder setOnCancelClickListener(String cancelText, onCancelClickListener onCancelclickListener) {
            this.mCancelText = cancelText;
            this.mOnCcancelClickListener = onCancelclickListener;
            return this;
        }

        public MyDialog build() {
            return new MyDialog(mContext, mTitle, mMessage, mConfirmText, mCancelText,
                    mOnConfirmClickListener, mOnCcancelClickListener);
        }
    }
}