package com.qdjxd.examination;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;
/**
 * 参数设置界面
 * @author asus
 *
 */
public class ParamSettingActivity extends BaseActivity {
	Button logoutBtn;//注销登陆
	ImageView backImg;//回退按钮
	TextView webIpText;//ip输入框
	Button saveBtn;//保存按钮
	String webIpStr;//ip
	EditText padType; //pad类型
	EditText padVersion; //版本号
	EditText padNumber ;//终端串号
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_param_setting);
		initView();
	}
	private void initView(){
		/*回退按钮*/
		backImg = (ImageView) findViewById(R.id.come_back);
		backImg.setOnClickListener(listener);
		/*ip输入框*/
		webIpText = (TextView) findViewById(R.id.webIP);
		webIpStr = BaseInfo.getIpAddress(ParamSettingActivity.this);
		//
		if("".equals(webIpStr)){
			webIpText.setText("");
			DebugLog.v(webIpStr);
		}else{
			webIpText.setText(webIpStr);
			DebugLog.v(webIpStr);
		}
		
		/*保存按钮*/
		saveBtn  =(Button) findViewById(R.id.btnSave);
		saveBtn.setOnClickListener(listener);
		/*注销按钮*/
		logoutBtn = (Button) findViewById(R.id.logout_btn);
		if(BaseInfo.IsLogin(sp)){
			logoutBtn.setVisibility(View.VISIBLE);
		}else{
			logoutBtn.setVisibility(View.GONE);
		}
		logoutBtn.setOnClickListener(listener);
		/*默认PAD型号*/
		padType = (EditText) findViewById(R.id.padType);
		padType.setText(Build.MODEL);
		/*版本号*/
		padVersion = (EditText) findViewById(R.id.padVersion);
		try {
			padVersion.setText(getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		//默认串号
		padNumber = (EditText) findViewById(R.id.padNumber);
		TelephonyManager telephonemanage = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		try {
			String PADid = telephonemanage.getDeviceId();
			if (PADid == null || "".equals(PADid)) {
				padNumber.setText(getMACAdress());
			} else {
				padNumber.setText(PADid);
			}
		} catch (Exception e) {
			padNumber.setText(getMACAdress());
			e.printStackTrace();
		}
	}
	/**获取移动设备串号*/
	private String getMACAdress() {
		 WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
		 WifiInfo info = wifi.getConnectionInfo();  
		 return info.getMacAddress();
	}
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.come_back:
				finish();
				break;
			case R.id.logout_btn:
				BaseInfo.setLoginState(sp, false);
				Intent intent = new Intent(ParamSettingActivity.this,MainActivity.class);
				setResult(1025, intent);
				finish();
				break;
			case R.id.btnSave:
				if(checkIP()){
					webIpStr = webIpText.getText().toString();
					BaseInfo.setIpAddress(ParamSettingActivity.this, webIpStr);
					BaseInfo.setWebURL(ParamSettingActivity.this,BaseInfo.getWebURLByIP(webIpStr));
					MsgUtil.ShowErrMsg("保存成功", ParamSettingActivity.this);
				}
				break;
			default:
				break;
			}
		}
	};
	/**检查ip地址是否正确*/
	private boolean checkIP(){
		String ipStr = webIpText.getText().toString().trim();
		if ("".equals(ipStr)) {
			MsgUtil.ShowErrMsg("IP不能为空！", ParamSettingActivity.this);
			return false;
		}
		String[] ip = ipStr.split(":");
		String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
		if(ip[0].matches(regex)){
			return true;
		}else{
			MsgUtil.ShowErrMsg("IP格式错误，请重新填写！", ParamSettingActivity.this);
			return false;
		}
	}
}
