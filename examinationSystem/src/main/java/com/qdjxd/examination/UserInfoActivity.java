package com.qdjxd.examination;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.baseInfo.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends BaseActivity implements OnClickListener{

	@BindView(R.id.user_info_username)
	TextView username;

	@BindView(R.id.user_layout)
	LinearLayout userLayout;

	@BindView(R.id.user_info_back)
	Button btnBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ButterKnife.bind(this);
		initView();
	}
	private void initView(){
		btnBack.setOnClickListener(this);
		userLayout.setOnClickListener(this);
		username.setText(UserInfo.getUserAlias(sp));
		username.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			case R.id.user_info_back:
				finish();
				break;
			case R.id.user_layout:
				intent.setClass(UserInfoActivity.this, MyInfoActivity.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}
}
