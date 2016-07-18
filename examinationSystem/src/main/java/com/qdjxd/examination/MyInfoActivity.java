package com.qdjxd.examination;

import com.qdjxd.examination.baseInfo.UserInfo;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInfoActivity extends BaseActivity {
    @BindView(R.id.my_name_textview)
    TextView tv_myName;
    @BindView(R.id.my_sex_textview)
    TextView tv_mySex;
    @BindView(R.id.my_dept_textview)
    TextView tv_myDept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        ButterKnife.bind(this);
        tv_myName.setText(UserInfo.getUserAlias(sp));
        tv_mySex.setText(UserInfo.getUserSex(sp));
        tv_myDept.setText(UserInfo.getUserOrgName(sp));
    }
}
