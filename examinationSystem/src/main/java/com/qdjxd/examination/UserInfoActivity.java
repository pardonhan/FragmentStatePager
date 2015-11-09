package com.qdjxd.examination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.baseInfo.UserInfo;

public class UserInfoActivity extends BaseActivity {
	TextView username;
	LinearLayout userLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		this.findViewById(R.id.user_info_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initView();
	}
	private void initView(){
		userLayout = (LinearLayout) findViewById(R.id.user_layout);
		userLayout.setOnClickListener(listener);
		username = (TextView) findViewById(R.id.user_info_username);
		username.setText(UserInfo.getUserAlias(sp));
		username.setOnClickListener(listener);
	}
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.user_layout:
				intent.setClass(UserInfoActivity.this, MyInfoActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
}
