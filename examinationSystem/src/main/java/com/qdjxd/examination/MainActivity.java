/*
 * 文件名：DownLoadExamActivity
 * 版权：
 * 描述：用于下载试题
 * 创建人： hanfl
 * 创建时间：忘记了
 * 修改人:hanfl
 * 修改时间：2015-08-28
 * 修改单号：
 * 修改内容：
 */
package com.qdjxd.examination;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.baseInfo.UserInfo;
import com.qdjxd.examination.examacitvity.AutonomousExamActivity;
import com.qdjxd.examination.examacitvity.OrdinalExamActivity;
import com.qdjxd.examination.examacitvity.PracticeExamActivity;
import com.qdjxd.examination.examacitvity.QuestionSelectActivity;
import com.qdjxd.examination.examacitvity.RandomExamActivity;
import com.qdjxd.examination.examacitvity.specialtype.SpecialTypeExamActivity;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.gossipview.GossipItem;
import com.qdjxd.examination.gossipview.GossipView;
import com.qdjxd.examination.gossipview.GossipView.OnPieceClickListener;
import com.qdjxd.examination.infoactivity.CollectExamActivity;
import com.qdjxd.examination.infoactivity.MyWrongExamActivity;
import com.qdjxd.examination.infoactivity.RecordExamActivity;
import com.qdjxd.examination.infoactivity.TipExamActivity;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;
import com.qdjxd.examination.views.CircleImageView;
import com.qdjxd.examination.views.SlidingMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
	private SlidingMenu menu;
	private LinearLayout userLogin,paramSetting,li_downloadExam;
	private TextView user_name;
	private TextView menu_tip;

	//顶部菜单
	private TextView exam_tip,exam_notes,my_wrong,my_collect;
	//底部菜单
	private TextView right_num,wrong_num,undone_num,collect_num;

	private final int login_result = 1024;
	private final int logout_result = 1025;
	private final int random_result = 1026;
	String [] strs = {"章节练习","随机练习","专项练习","自主考试","顺序练习","问题查询"} ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu = (SlidingMenu) findViewById(R.id.id_menuss);
		CircleImageView turn_menu = (CircleImageView) findViewById(R.id.Turn_menu);
		turn_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
		initView();
		MyAsyncTask myAsyncTask = new MyAsyncTask();
		myAsyncTask.execute();
	}
	
	@Override
	protected void onPause() {
		if(BaseInfo.IsLogin(sp)){
			DebugLog.i(UserInfo.getUserAlias(sp));
			user_name.setText(UserInfo.getUserAlias(sp));
			menu_tip.setText(R.string.tip_login);
		}
		super.onPause();
	}
	private void initView() {
		userLogin = (LinearLayout) findViewById(R.id.user_login);
		userLogin.setOnClickListener(listener);
		user_name = (TextView) findViewById(R.id.user_name);
		menu_tip = (TextView) findViewById(R.id.menu_tip);
		if(BaseInfo.IsLogin(sp)){
			DebugLog.i(UserInfo.getUserAlias(sp));
			user_name.setText(UserInfo.getUserAlias(sp));
			menu_tip.setText(R.string.tip_login);
		}
		
		li_downloadExam = (LinearLayout) findViewById(R.id.download_exam);
		li_downloadExam.setOnClickListener(listener);
		
		paramSetting = (LinearLayout) findViewById(R.id.param_setting);
		paramSetting.setOnClickListener(listener);


		GossipView gossipView = (GossipView)findViewById(R.id.gossipview);
		final List<GossipItem> items =new ArrayList<GossipItem>();
		for(int i = 0; i < strs.length; i++) { 
			GossipItem item = new GossipItem(strs[i],1);
			items.add(item);
		}
		gossipView.setItems(items);
		gossipView.setNumber(1);
		gossipView.setTextStr("模拟考试");
		gossipView.setOnPieceClickListener(pListener);

		//顶部菜单初始化和添加监听事件
		exam_tip = (TextView) findViewById(R.id.exam_tip);
		exam_tip.setOnClickListener(dListener);
		exam_notes = (TextView) findViewById(R.id.exam_notes);
		exam_notes.setOnClickListener(dListener);
		my_wrong = (TextView) findViewById(R.id.my_wrong);
		my_wrong.setOnClickListener(dListener);
		my_collect = (TextView) findViewById(R.id.my_collect);
		my_collect.setOnClickListener(dListener);

		//底部菜单 right_num,wrong_num,undone_num,collect_num
		right_num = (TextView) findViewById(R.id.right_num);
		right_num.setTextSize(22);
		right_num.setTextColor(getResources().getColor(R.color.num_color));
		wrong_num = (TextView) findViewById(R.id.wrong_num);
		wrong_num.setTextSize(22);
		wrong_num.setTextColor(getResources().getColor(R.color.cardinal));
		undone_num = (TextView) findViewById(R.id.undone_num);
		undone_num.setTextColor(getResources().getColor(R.color.yellow));
		undone_num.setTextSize(22);
		collect_num = (TextView) findViewById(R.id.collect_num);
		collect_num.setTextColor(getResources().getColor(R.color.num_color));
		collect_num.setTextSize(22);
	}
	//首页饼图点击事件
	OnPieceClickListener pListener = new OnPieceClickListener() {
		@Override
		public void onPieceClick(int index) {
			Intent intent = new Intent();
			switch (index) {
				case -1:
					//模拟考试
					intent.setClass(MainActivity.this, PracticeExamActivity.class);
					startActivityForResult(intent,random_result);
					break;
				case 0:
					//章节练习
					break;
				case 1:
					//随机练习
					intent.setClass(MainActivity.this, RandomExamActivity.class);
					startActivityForResult(intent,random_result);
					break;
				case 2:
					//专项练习
					intent.setClass(MainActivity.this, SpecialTypeExamActivity.class);
					startActivityForResult(intent,random_result);
					break;
				case 3:
					//自主考试
					intent.setClass(MainActivity.this, AutonomousExamActivity.class);
					startActivity(intent);
					break;
				case 4:
					//顺序练习
					intent.setClass(MainActivity.this, OrdinalExamActivity.class);
					startActivityForResult(intent,random_result);
					break;
				case 5:
					//问题查询
					intent.setClass(MainActivity.this, QuestionSelectActivity.class);
					startActivityForResult(intent,random_result);
					break;
				default:
					break;
			}
		}
	}; 
	//首页及菜单页点击事件
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
				case R.id.user_login:
					//判断是登录还是进入用户信息页面
					if(BaseInfo.IsLogin(sp)){
						// User Info Activity
						intent.setClass(MainActivity.this, UserInfoActivity.class);
						startActivity(intent);
					}else{
						//Login Activity
						intent.setClass(MainActivity.this, LoginActivity.class);
						startActivityForResult(intent, login_result);
					}
					break;
				case R.id.exam_storage:
					//intent.setClass(MainActivity.this, DownLoadExamActivity.class);
					startActivity(intent);
					break;
				case R.id.download_exam:
					intent.setClass(MainActivity.this, DownLoadExamActivity.class);
					startActivity(intent);
					break;
				case R.id.param_setting:
					intent.setClass(MainActivity.this, ParamSettingActivity.class);
					startActivityForResult(intent,logout_result);
					break;
				default:
					break;
			}
		}
	};
	//底部菜单点击事件
	OnClickListener dListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
				case R.id.exam_tip:
					intent.setClass(MainActivity.this, TipExamActivity.class);
					startActivity(intent);
					break;
				case R.id.exam_notes:
					intent.setClass(MainActivity.this, RecordExamActivity.class);
					startActivity(intent);
					break;
				case R.id.my_wrong:
					intent.setClass(MainActivity.this, MyWrongExamActivity.class);
					startActivity(intent);
					break;
				case R.id.my_collect:
					intent.setClass(MainActivity.this, CollectExamActivity.class);
					startActivity(intent);
					break;
				default:
					break;
			}
		}
	};
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				user_name.setText(UserInfo.getUserAlias(sp));
				menu_tip.setText(R.string.tip_login);
				break;
			case logout_result:
				user_name.setText(R.string.login_now);
				menu_tip.setText(R.string.tip);
				break;
			case random_result:
				MsgUtil.ShowErrMsg("请先下载试题", MainActivity.this);
				break;
			default:
				break;
		}
	};
	//异步加载
	class MyAsyncTask extends AsyncTask<String, String, Map<String,String>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Map<String,String> doInBackground(String... params) {
			Map<String,String> map = DataBaseUtils.selectNumbers(MainActivity.this);
			return map;
		}

		@Override
		protected void onPostExecute(Map<String,String> map) {
			super.onPostExecute(map);
			//right_num,wrong_num,undone_num,collect_num
			if(map.size()>0){
				right_num.setText(map.get("right_num"));
				wrong_num.setText(map.get("wrong_num"));
				undone_num.setText(map.get("undone_num"));
				collect_num.setText(map.get("collect_num"));
			}
		}
	}
}
