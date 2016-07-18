package com.qdjxd.examination;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.baseInfo.UserInfo;
import com.qdjxd.examination.common.Common;
import com.qdjxd.examination.common.CommonFactory;
import com.qdjxd.examination.examacitvity.PracticeExamActivity;
import com.qdjxd.examination.utils.MsgUtil;
import com.qdjxd.examination.utils.NetWorkUtil;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.editText1)
    EditText usercode;
    @BindView(R.id.editText2)
    EditText password;
    @BindView(R.id.button1)
    Button loginButton;
    private Dialog m_Dialog;
    @BindView(R.id.save_user_code)
    CheckBox saveUsernameCheckbox;
    @BindView(R.id.save_pass_word)
    CheckBox savePasswordCheckbox;
    private static final int msg_success = 1002;
    private boolean isPractice = false;
    private LoginHandler loginhandler = new LoginHandler(new WeakReference<>(this));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        Intent intent = getIntent();
        isPractice = intent.getBooleanExtra("practice", false);
        initView();
    }

    private void initView() {
        //usercode.setText("430603196603110532");
        saveUsernameCheckbox.setOnCheckedChangeListener(cListener);
        savePasswordCheckbox.setOnCheckedChangeListener(cListener);
        if (BaseInfo.IsSaveName(sp)) {
            usercode.setText(UserInfo.getUserName(sp));
            saveUsernameCheckbox.setChecked(true);
        }
        if (BaseInfo.IsSavePwd(sp)) {
            password.setText(UserInfo.getUserPwd(sp));
            savePasswordCheckbox.setChecked(true);
        }
        loginButton.setOnClickListener(this);
    }

    OnCheckedChangeListener cListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton cView, boolean isChecked) {
            switch (cView.getId()) {
                case R.id.save_user_code:
                    BaseInfo.setSaveName(sp, isChecked);
                    savePasswordCheckbox.setClickable(isChecked);
                    if (!isChecked) {
                        savePasswordCheckbox.setChecked(false);
                    }
                    break;
                case R.id.save_pass_word:
                    BaseInfo.setSavePwd(sp, isChecked);
                    break;
                default:
                    break;
            }

        }
    };

    /**
     * 登录方法
     */
    private void login() {
        final String u = usercode.getText().toString();
        final String p = password.getText().toString();
        if ("".equals(u) || "".equals(p)) {
            MsgUtil.ShowErrMsg("用户名和密码不能为空！", LoginActivity.this);
            return;
        }

        if (!NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
            MsgUtil.ShowErrMsg("无网络连接！", LoginActivity.this);
            return;
        }
        if ("".equals(BaseInfo.getIpAddress(LoginActivity.this))) {
            MsgUtil.ShowErrMsg("请配置IP地址！", LoginActivity.this);
            return;
        }
        if (BaseInfo.IsSavePwd(sp)) {
            UserInfo.setUserPwd(p, sp);
        }
        if (m_Dialog == null) {
            m_Dialog = MsgUtil.ShowLoadDialog(this, "请等待...", "正在登录，请稍候...", true);
        } else {
            m_Dialog.show();
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    Common common = CommonFactory.getCommon(sp);
                    if (common.login(LoginActivity.this, u, MsgUtil.ToMD5(p), sp)) {
                        loginhandler.sendMessage(loginhandler.obtainMessage(msg_success, u));
                    } else {
                        loginhandler.sendEmptyMessage(1);//登录失败
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    private static class LoginHandler extends Handler {

        private WeakReference<LoginActivity> weakReference;

        protected LoginHandler(WeakReference<LoginActivity> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LoginActivity activity = weakReference.get();
            if (activity == null) {
                return;
            }
            if (activity.m_Dialog != null)
                activity.m_Dialog.dismiss();
            switch (msg.what) {
                case msg_success:
                    if (activity.isPractice) {
                        BaseInfo.setLoginState(activity.sp, true);
                        Intent intent = new Intent(activity, PracticeExamActivity.class);
                        activity.setResult(1024, intent);
                        activity.finish();
                    } else {
                        BaseInfo.setLoginState(activity.sp, true);
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.setResult(RESULT_OK, intent);
                        activity.finish();
                    }
                    break;
                case 1:
                    MsgUtil.ShowToast("登录失败！", activity);
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                //进行登录判断
                login();
                break;
            default:
                break;
        }
    }
}
