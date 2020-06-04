package com.ylh.huqidiary.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import com.ylh.huqidiary.R;
import com.ylh.huqidiary.utils.AppManager;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.edit_account)
    EditText mEditAccount;
    @Bind(R.id.edit_password)
    EditText mEditPassword;
    @Bind(R.id.btn_login)
    Button mBtnLogin;
    @Bind(R.id.btn_register)
    Button mBtnRegister;
    @Bind(R.id.btn_forget_password)
    Button mBtnForgetPassword;
    @Bind(R.id.cb_rememberPassword)
    CheckBox mCbRememberPassword;

    private SharedPreferences sp = null;
    final String USER_NAME = "user_name";
    final String USER_PASSWORD = "user_password";
    final String PASSWORD_CHECKED = "password_checked";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
    }

    private void initData() {
        // SharedPreference存储用户信息
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //设置【账号】与【密码】到文本框，并勾选【记住密码】
        if (sp.getBoolean(PASSWORD_CHECKED, false)) {
            mEditAccount.setText(sp.getString(USER_NAME, ""));
            mEditPassword.setText(sp.getString(USER_PASSWORD, ""));
            mCbRememberPassword.setChecked(true);
        }
        if(sp.getString(USER_NAME, "") != "" && sp.getBoolean(PASSWORD_CHECKED, false)){
            MainActivity.startActivity(this);
            finish();
        }

    }

    @OnClick({R.id.btn_login,R.id.btn_register,R.id.btn_forget_password})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String _account = mEditAccount.getText().toString().trim();
                String _password = mEditPassword.getText().toString().trim();
                if(_account.equals("")){
                    Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(_password.equals("")){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (_account.equals("13128244164") && _password.equals("mamazi2277")) {   //登录成功
                    Editor editor = sp.edit();
                    if (mCbRememberPassword.isChecked()) { //记住账号与密码
                        editor.putBoolean(PASSWORD_CHECKED, true);
                        editor.putString(USER_NAME, _account);
                        editor.putString(USER_PASSWORD, _password);
                    } else { //清空数据
                        editor.clear();
                    }
                    editor.apply();
                    MainActivity.startActivity(this);
                    finish();
                } else {
                    Toast.makeText(this, "账号或密码不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.btn_forget_password:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }
}
