package com.qdjxd.examination.examacitvity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;

public class ExamResultsActivity extends BaseActivity {
	private TextView check_exam_tv,check_wrong_tv,exam_grade_tv,exam_usetime_tv;
	private ImageView close_activity_img;
	private int grade,useTime;
	
	private ArrayList<QuestionInfo> mList = new ArrayList<QuestionInfo>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_result);
		
		close_activity_img = (ImageView) findViewById(R.id.close_activity);
		close_activity_img.setOnClickListener(listener);
		
		Intent intent = this.getIntent();
		grade = intent.getIntExtra("grade", 0);
		useTime = intent.getIntExtra("time", 0);
		DebugLog.v(grade+"||"+useTime);
		mList = (ArrayList<QuestionInfo>) intent.getSerializableExtra("list");
		
		exam_grade_tv = (TextView) findViewById(R.id.exam_grade);
		exam_grade_tv.setText(""+grade);
		exam_usetime_tv = (TextView) findViewById(R.id.exam_usetime);
		exam_usetime_tv.setText(timeToString(useTime));
		
		check_exam_tv = (TextView) findViewById(R.id.check_exam_tv);
		check_exam_tv.setOnClickListener(listener);
		check_wrong_tv = (TextView) findViewById(R.id.check_wrong_tv);
		check_wrong_tv.setOnClickListener(listener);
		saveExamResult();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK ) {
			// DO NOTHING
		}
		return false;
	};
	
	private void saveExamResult(){
		new Thread(){
			public void run() {
				DataBaseUtils.insertResult(ExamResultsActivity.this, mList,"1");
			}
		}.start();
	}
	
	//监听事件
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			switch (arg0.getId()) {
				case R.id.close_activity:
					finish();
					break;
				case R.id.check_exam_tv:
					intent.setClass(ExamResultsActivity.this, CheckExamActivity.class);
					intent.putExtra("mark", "all");
					intent.putExtra("allList", mList);
					startActivity(intent);
					break;
				case R.id.check_wrong_tv:
					ArrayList<QuestionInfo> wrongList = new ArrayList<QuestionInfo>();
					for(QuestionInfo qsf : mList){
						if(qsf.wrongModel==0){
							wrongList.add(qsf);
						}
					}
					if(wrongList.size()>0){
						intent.setClass(ExamResultsActivity.this, CheckExamActivity.class);
						intent.putExtra("mark", "wrong");
						intent.putExtra("wrongList", wrongList);
						startActivity(intent);
					}else{
						MsgUtil.ShowToast("当前考试没有错题哦！", ExamResultsActivity.this);
					}
					break;
				default:
					break;
			}
		}
	};
	private String timeToString(int useTime){
		int min = 0;
		int sec = 0;
		String m,s;
		if(useTime>60){
			min = useTime/60;
			sec = useTime - min*60;
			m = "" + min;
			s = "" + sec;
			if(min<10){
				m = "0"+min;
			}
			if(sec<10){
				s = "0" + sec;
			}
		}else{
			m = "00";
			s = "" + useTime;
			if(useTime < 10){
				s = "0" + useTime;
			}
		}
		return m + ":" + s;
	}
}
