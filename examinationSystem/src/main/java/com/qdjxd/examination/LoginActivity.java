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

public class LoginActivity extends BaseActivity {
	private EditText usercode,password;
	private Button loginButton;
	private Dialog m_Dialog;
	private CheckBox saveUsernameCheckbox,savePasswordCheckbox;
	private static final int msg_success = 1002;
	private boolean isPractice = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
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
	
	private void initView(){
		usercode = (EditText) findViewById(R.id.editText1);
		usercode.setText("430603196603110532");
		password = (EditText) findViewById(R.id.editText2);
		saveUsernameCheckbox = (CheckBox) findViewById(R.id.save_user_code);
		saveUsernameCheckbox.setOnCheckedChangeListener(cListener);
		savePasswordCheckbox = (CheckBox) findViewById(R.id.save_pass_word);
		savePasswordCheckbox.setOnCheckedChangeListener(cListener);
		if (BaseInfo.IsSaveName(sp)) {
			usercode.setText(UserInfo.getUserName(sp));
			saveUsernameCheckbox.setChecked(true);
		}
		if(BaseInfo.IsSavePwd(sp)){
			password.setText(UserInfo.getUserPwd(sp));
			savePasswordCheckbox.setChecked(true);
		}
		loginButton = (Button) findViewById(R.id.button1);
		loginButton.setOnClickListener(listener);
		
	}
	OnCheckedChangeListener cListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton cView, boolean isChecked) {
			switch (cView.getId()) {
			case R.id.save_user_code:
				BaseInfo.setSaveName(sp, isChecked);
				savePasswordCheckbox.setClickable(isChecked);
				if(!isChecked){
					savePasswordCheckbox.setChecked(isChecked);
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
	OnClickListener listener = new OnClickListener() {
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
	};
	private void login(){
		final String u = usercode.getText().toString();
		final String p = password.getText().toString();
		if ("".equals(u) || "".equals(p)) {
    		MsgUtil.ShowErrMsg("用户名和密码不能为空！", LoginActivity.this);
    		return;
    	}
		
		if ( !NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
    		MsgUtil.ShowErrMsg("无网络连接！", LoginActivity.this);
    		return;
    	}
		if("".equals(BaseInfo.getIpAddress(LoginActivity.this))){
			MsgUtil.ShowErrMsg("请配置IP地址！", LoginActivity.this);
    		return;
		}
		if (BaseInfo.IsSavePwd(sp)) {
			UserInfo.setUserPwd(p, sp);
		}
		if(m_Dialog == null){
			m_Dialog = MsgUtil.ShowLoadDialog(this, "请等待...", "正在登录，请稍候...",true);
		}
		else{
			m_Dialog.show();
		}
		new Thread() {
			@Override
			public void run() {
				try{
					Common common = CommonFactory.getCommon(sp);
					if (common.login(LoginActivity.this, u, MsgUtil.ToMD5(p), sp)) {
						loginhandler.sendMessage(loginhandler.obtainMessage(msg_success, u));
					} else {
						loginhandler.sendEmptyMessage(1);//登录失败
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				super.run();
			}
    	}.start();
	}
    private Handler loginhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(m_Dialog!=null)
				m_Dialog.dismiss();
			if (msg.what == msg_success) {
				if(isPractice){
					BaseInfo.setLoginState(sp,true);
					Intent intent = new Intent(LoginActivity.this,PracticeExamActivity.class);
					setResult(1024, intent);
					finish();
				}else{
					BaseInfo.setLoginState(sp,true);
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					setResult(RESULT_OK, intent);
					finish();
				}
			} else if(msg.what==1){
				MsgUtil.ShowToast("登录失败！", LoginActivity.this);
			}
		}
    };
}
