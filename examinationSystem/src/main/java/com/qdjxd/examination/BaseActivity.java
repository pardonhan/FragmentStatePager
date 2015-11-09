package com.qdjxd.examination;

import java.io.File;

import com.qdjxd.examination.utils.DensityUtil;
import com.qdjxd.examination.utils.SharedPreferencesHelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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

	public String DB_PATH;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sp = SharedPreferencesHelper.getDefault(this);
		du = new DensityUtil(this);
	}

	protected void onPause() {
		super.onPause();
		if (finishActivityOnPause) {
			finish();
		}
	}

	protected LinearLayout menu;
	protected LinearLayout main;
	protected LayoutParams menuParams;
	protected LayoutParams mainParams;
	protected boolean mIsShow = true;

	/**
	 * 判断SD卡上的文件夹是否存在
	 * */
	public boolean isFileExist(String str) {
		File file = new File(str);
		return file.exists();
	}


	/**
	 * 侧边栏菜单动画效果
	 * 
	 * @author huazi
	 * 
	 */
	protected class ShowMenuAsyncTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			int leftMargin = mainParams.leftMargin;

			while (true) {
				leftMargin += params[0];
				if (params[0] > 0 && leftMargin >= 0) {
					leftMargin = 0;
					break;
				} else if (params[0] < 0 && leftMargin <= -menuParams.width) {
					leftMargin = -menuParams.width;
					break;
				}
				publishProgress(leftMargin);
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			mainParams.leftMargin = values[0];
			main.setLayoutParams(mainParams);
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			mainParams.leftMargin = result;
			main.setLayoutParams(mainParams);
		}

	}

	protected void showMenu(boolean isShow) {
		if (isShow) {
			mIsShow = true;
			new ShowMenuAsyncTask().execute(50);
		} else {
			mIsShow = false;
			new ShowMenuAsyncTask().execute(-50);
		}
	}

	@Override
	public void onBackPressed() {
		if (!mIsShow) {
			showMenu(!mIsShow);
			return;
		}
		super.onBackPressed();
	}
}
