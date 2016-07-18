package com.qdjxd.examination;


import com.qdjxd.examination.utils.DensityUtil;
import com.qdjxd.examination.utils.SharedPreferencesHelper;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	/**
	 * 当跳转的时候时候是否关闭当前Activity
	 */
	public boolean finishActivityOnPause = false;

	/**
	 * 轻量数据储存方式
	 */
	public SharedPreferencesHelper sp;

	/**
	 * 关于屏幕参数的类
	 */
	public DensityUtil du;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = SharedPreferencesHelper.getDefault(this);
		du = new DensityUtil(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (finishActivityOnPause) {
			finish();
		}
	}
}
