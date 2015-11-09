package com.qdjxd.examination.examacitvity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.utils.DebugLog;
/**
 * 专项练习 用户选择练习项目
 * @author asus
 *
 */
public class SpecialTypeExamActivity extends BaseActivity {
	private ImageView backImage;
	private TextView panduanti_tv,danxuanti_tv,duoxuanti_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_type);
		backImage = (ImageView) findViewById(R.id.back);
		backImage.setOnClickListener(listener);
		panduanti_tv = (TextView) findViewById(R.id.panduanti_tv);
		panduanti_tv.setOnClickListener(listener);
		danxuanti_tv = (TextView) findViewById(R.id.danxuanti_tv);
		danxuanti_tv.setOnClickListener(listener);
		duoxuanti_tv = (TextView) findViewById(R.id.duoxuanti_tv);
		duoxuanti_tv.setOnClickListener(listener);
	}
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.back:
				DebugLog.v("finish");
				finish();
				break;
			case R.id.panduanti_tv:
				DebugLog.v("判断题");
				intent.setClass(SpecialTypeExamActivity.this, RandomExamActivity.class);
				intent.putExtra("typeid", "3");
				startActivity(intent);
				break;
			case R.id.danxuanti_tv:
				DebugLog.v("单选题");
				intent.setClass(SpecialTypeExamActivity.this, RandomExamActivity.class);
				intent.putExtra("typeid", "1");
				startActivity(intent);
				break;
			case R.id.duoxuanti_tv:
				DebugLog.v("多选题");
				intent.setClass(SpecialTypeExamActivity.this, RandomExamActivity.class);
				intent.putExtra("typeid", "2");
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
}
