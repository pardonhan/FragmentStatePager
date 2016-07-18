package com.qdjxd.examination;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;
import com.qdjxd.examination.utils.PublicUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 参数设置界面
 *
 * @author asus
 */
public class ParamSettingActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.logout_btn)
    Button logoutBtn;//注销登陆
    @BindView(R.id.come_back)
    ImageView backImg;//回退按钮
    @BindView(R.id.webIP)
    TextView webIpText;//ip输入框
    @BindView(R.id.btnSave)
    Button saveBtn;//保存按钮
    @BindView(R.id.padType)
    EditText padType; //pad类型
    @BindView(R.id.padVersion)
    EditText padVersion; //版本号
    @BindView(R.id.padNumber)
    EditText padNumber;//终端串号

    String webIpStr;//ip

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        /*回退按钮*/
        backImg.setOnClickListener(this);
        /*ip输入框*/
        webIpStr = BaseInfo.getIpAddress(ParamSettingActivity.this);
        //
        if ("".equals(webIpStr)) {
            webIpText.setText("");
            DebugLog.v(webIpStr);
        } else {
            webIpText.setText(webIpStr);
            DebugLog.v(webIpStr);
        }
		
		/*保存按钮*/
        saveBtn.setOnClickListener(this);
		/*注销按钮*/
        if (BaseInfo.IsLogin(sp)) {
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            logoutBtn.setVisibility(View.GONE);
        }
        logoutBtn.setOnClickListener(this);
		/*默认PAD型号*/
        padType.setText(Build.MODEL);
		/*版本号*/
        try {
            padVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        //默认串号
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

    /**
     * 获取移动设备串号
     */
    private String getMACAdress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.come_back:
                finish();
                break;
            case R.id.logout_btn:
                BaseInfo.setLoginState(sp, false);
                Intent intent = new Intent(ParamSettingActivity.this, MainActivity.class);
                setResult(1025, intent);
                finish();
                break;
            case R.id.btnSave:
                String ipStr = webIpText.getText().toString().trim();
                if (TextUtils.isEmpty(ipStr)) {
                    MsgUtil.ShowErrMsg("IP不能为空！", ParamSettingActivity.this);
                } else {
                    if (PublicUtils.checkIP(ipStr)) {
                        webIpStr = webIpText.getText().toString();
                        BaseInfo.setIpAddress(ParamSettingActivity.this, webIpStr);
                        BaseInfo.setWebURL(ParamSettingActivity.this, BaseInfo.getWebURLByIP(webIpStr));
                        MsgUtil.ShowErrMsg("保存成功", ParamSettingActivity.this);
                    } else {
                        MsgUtil.ShowErrMsg("IP格式错误，请重新填写！", ParamSettingActivity.this);
                    }
                }
                break;
            default:
                break;
        }
    }
}
