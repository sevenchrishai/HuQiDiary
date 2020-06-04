package com.ylh.huqidiary.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.OnClick;
import com.ylh.huqidiary.R;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView mIvBack;
    @Bind(R.id.edit_account)
    EditText mEditAccount;
    @Bind(R.id.edit_password)
    EditText mEditPassword;
    @Bind(R.id.edit_confirm_password)
    EditText mEditConfirmPassword;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @OnClick({R.id.iv_back,R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_register:
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
                startActivity(LoginActivity.class);
                finish();
                break;
        }

    }
}
