package com.qdjxd.examination.examacitvity;

import java.util.ArrayList;
import java.util.List;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.os.Message;
//import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.examacitvity.fragment.QuestionFragment;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;

public class AutonomousExamActivity extends BaseActivity{
	private ViewPager mViewPager;
	private Dialog m_Dialog;
	private List<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();

	private List<QuestionFragment> questionFragmentList;

	public static final String QUESTION_INFO_LIST = "QUESTION_INFO_LIST";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_autonomous);
		this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		FragmentManager manager = getFragmentManager();
		questionFragmentList = new ArrayList<QuestionFragment>();
		Intent intent = getIntent();
		questionInfoList = (ArrayList<QuestionInfo>) intent.getSerializableExtra(QUESTION_INFO_LIST);
		if(questionInfoList!=null) {
			for (QuestionInfo item : questionInfoList) {
				questionFragmentList.add(new QuestionFragment(item));
			}
		}
		mViewPager = (ViewPager)findViewById(R.id.viewpager);

		mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
			@Override
			public Fragment getItem(int i) {
				DebugLog.i(i);
				return questionFragmentList.get(i);
			}

			@Override
			public int getCount() {
				return questionInfoList.size();
			}
		});
		mViewPager.setCurrentItem(0);
	}

	private class AutoNomousAdapter extends FragmentStatePagerAdapter
		implements ViewPager.OnPageChangeListener{
		List<QuestionInfo> list = new ArrayList<QuestionInfo>();
		public AutoNomousAdapter(FragmentManager fm,List<QuestionInfo> list){
			super(fm);
			this.list = list;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {

		}
		@Override
		public Fragment getItem(int arg0) {
			return questionFragmentList.get(arg0);
		}

		//获取要滑动的控件数量，此处是是根据题目数量来确定
		@Override
		public int getCount() {
			return list.size();
		}
	}
}
