package com.qdjxd.examination;

import com.qdjxd.examination.baseInfo.UserInfo;

import android.os.Bundle;
import android.widget.TextView;

public class MyInfoActivity extends BaseActivity {
	private TextView tv_myName,tv_mySex,tv_myDept;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		tv_myName = (TextView) findViewById(R.id.my_name_textview);
		tv_myName.setText(UserInfo.getUserAlias(sp));
		tv_mySex = (TextView) findViewById(R.id.my_sex_textview);
		tv_mySex.setText(UserInfo.getUserSex(sp));
		tv_myDept = (TextView) findViewById(R.id.my_dept_textview);
		tv_myDept.setText(UserInfo.getUserOrgName(sp));
	}
}
