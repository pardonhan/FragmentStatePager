/*
 * 文件名：PracticeExamActivity
 * 版权：
 * 描述：模拟答题
 * 创建人： hanfl
 * 创建时间：2015-08-27
 * 修改人:hanfl
 * 修改时间：2015-08-28
 * 修改单号：
 * 修改内容：
 */
package com.qdjxd.examination.examacitvity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.MainActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.PopupWindowDialog;
import com.qdjxd.examination.utils.PublicUtils;
import com.qdjxd.examination.views.CircleTextView;
/**
 * 查看考试结果
 * 		模拟考试完成后跳转到考试结果，
 * 		点击查看试卷或只看错题进入此页面
 * @author hanfl
 *
 */
public class CheckExamActivity extends BaseActivity {
    		  // 总数  当前题目  114行   正确数量  错误数量   剩余数量
    private int count,current,li_size,rightNum,wrongNum,emptyNum;
	private Dialog m_Dialog;
	private ImageView img_practise;
	
	private List<LinearLayout> li_LinearLayout;
	private List<CircleTextView> ci_TextView;
	private List<TextView> tv_TextView;
	
	private TextView tv_question,tv_rightNum,tv_wrongNum,tv_emptyNum,tv_questionNum;
	private Button btn_next,btn_previous,btn_submit;//下一题，上一题，提交
	private LinearLayout li_down;
	
	private ArrayList<QuestionInfo> mList = new ArrayList<QuestionInfo>();
	private String[] check =  {"正确","错误"};
	private GridView gv_gridview;
	private ExamGridAdapter egadapter;
	private PopupWindow pwMyPopWindow;// popupwindow
	private PopupWindowDialog popDialog;//no next practice dialog
	
    @SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam_check);
		this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		current = 0;
		rightNum = 0;
		wrongNum = 0;
		emptyNum = 0;
		initView();
		initPopupWindow();
		
		Intent intent = getIntent();
		String mark = intent.getStringExtra("mark");
		DebugLog.v(mark);
		
		if(mark.equals("all")){
			ArrayList<QuestionInfo> _List = (ArrayList<QuestionInfo>) intent.getSerializableExtra("allList");
			emptyNum = _List.size();
			for(QuestionInfo qf : _List){
				if(qf.wrongModel==1){
					rightNum++;
				}else if(qf.wrongModel==0){
					wrongNum++;
				}
			}
			handler.sendMessage(handler.obtainMessage(1, _List));
		}
		if(mark.equals("wrong")){
			ArrayList<QuestionInfo> _List  = (ArrayList<QuestionInfo>) intent.getSerializableExtra("wrongList");
			emptyNum = _List.size();
			for(QuestionInfo qf : _List){
				if(qf.wrongModel==1){
					rightNum++;
				}else if(qf.wrongModel==0){
					wrongNum++;
				}
			}
			handler.sendMessage(handler.obtainMessage(1, _List));
		}
		new Handler().postDelayed(new Runnable() { 
		     @Override 
		     public void run() { 
		    	 tv_questionNum.performClick();
		     } 
		   }, 500);
		
    }
    
    private void initView(){
		popDialog = new PopupWindowDialog(CheckExamActivity.this, null);
		popDialog.btn_ok.setOnClickListener(listener);
		popDialog.btn_cancel.setOnClickListener(listener);
		
		img_practise = (ImageView) findViewById(R.id.practise);//题目类型
		tv_question = (TextView)findViewById(R.id.choose_question_content);//题目内容
		
		//题目列表
		tv_questionNum = (TextView) findViewById(R.id.tv_question_num);
		tv_questionNum.setOnClickListener(listener);

		//选项列表
		li_LinearLayout = new ArrayList<LinearLayout>();
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_a));
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_b));
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_c));
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_d));
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_e));
		li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_f));
		li_size = li_LinearLayout.size();
		
		ci_TextView = new ArrayList<CircleTextView>();
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_a));
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_b));
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_c));
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_d));
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_e));
		ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_f));
		
		tv_TextView = new ArrayList<TextView>();
		tv_TextView.add((TextView) findViewById(R.id.s_answerA));
		tv_TextView.add((TextView) findViewById(R.id.s_answerB));
		tv_TextView.add((TextView) findViewById(R.id.s_answerC));
		tv_TextView.add((TextView) findViewById(R.id.s_answerD));
		tv_TextView.add((TextView) findViewById(R.id.s_answerE));
		tv_TextView.add((TextView) findViewById(R.id.s_answerF));
		//下一题
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_next.setOnClickListener(listener);
        //上一题
        btn_previous = (Button)findViewById(R.id.btn_previous);
        btn_previous.setOnClickListener(listener);
        //提交
        btn_submit = (Button) findViewById(R.id.submitAs);
        btn_submit.setOnClickListener(listener);
	}
    //POPUPWINDOW FOR LIST OF EXAM NUMBER
  	private void initPopupWindow(){
  		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
  		View layout = inflater.inflate(R.layout.activity_gridview_exam, null);
  		RelativeLayout title_re = (RelativeLayout) layout.findViewById(R.id.title_re);
  		title_re.getBackground().setAlpha(180);//set titlebar background diaphaneity
  		li_down = (LinearLayout) layout.findViewById(R.id.answer_card);
  		li_down.setOnClickListener(listener);
  		
  		tv_rightNum = (TextView) layout.findViewById(R.id.right_answer);
  		tv_wrongNum = (TextView) layout.findViewById(R.id.wrong_answer);
  		tv_emptyNum = (TextView) layout.findViewById(R.id.empty_answer);
  		
  		//题目编号列表
  		gv_gridview = (GridView) layout.findViewById(R.id.gview);
  		pwMyPopWindow = new PopupWindow(layout,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
  		pwMyPopWindow.setFocusable(true);
  		pwMyPopWindow.setAnimationStyle(R.style.AnimationFade);//set Animation
  		egadapter = new ExamGridAdapter(CheckExamActivity.this);
  		gv_gridview.setAdapter(egadapter);
  	}
   
  	//show exam when you has choiced some question
  	private void showExamResult(){
  		QuestionInfo qf = mList.get(current);//题目数据
  		ArrayList<AnswerInfo> af = (ArrayList<AnswerInfo>) qf.answerItem;//选项数据
  		tv_question.setText((current+1)+"."+qf.qcontent);//题目编号和题目内容
  		
  		//初始化选项，全部隐藏，之后重新按照选项个数显示所需
  		for(int i =0;i<li_size;i++){
  			if(li_LinearLayout.get(i).getVisibility()==View.VISIBLE){
  				li_LinearLayout.get(i).setVisibility(View.GONE);
  			}
  		}
  		int asize = af.size();//选项个数
  		tv_questionNum.setText((current+1)+"/"+count);//页面右上角图标，显示  当前题目编号/题目数量
  		//初始化选项
  		for(int i = 0;i<asize;i++){
  			li_LinearLayout.get(i).setClickable(true);
  			li_LinearLayout.get(i).setOnClickListener(listener);
  			ci_TextView.get(i).setTextColor(Color.BLACK);
  			tv_TextView.get(i).setTextColor(Color.BLACK);
  		}
  		//选项个数大于0的为选择题，否则为判断题
  		if(asize>0){
  			//初始化选项
  			for(int i =0;i < asize;i++){
  				li_LinearLayout.get(i).setVisibility(View.VISIBLE);
  				tv_TextView.get(i).setText(af.get(i).itemcontent.toString());//显示选项内容
  			}
  			
  			if(qf.typeid.equals("1")){
  				// single choice question 
  				btn_submit.setVisibility(View.GONE);
  				img_practise.setImageResource(R.drawable.practise_danxuanti_day);
  				checkAnswer();
  			}
  			if(qf.typeid.equals("2")){
  				// multiple choice queation
  				btn_submit.setVisibility(View.VISIBLE);
  				img_practise.setImageResource(R.drawable.practise_duoxuanti_day);
  				checkMcqAnswer();
  			}
  		}else{
  			// true or false question
  			btn_submit.setVisibility(View.GONE);
  			img_practise.setImageResource(R.drawable.practise_panduanti_day);
  			for(int i =0;i < 2;i++){
  				li_LinearLayout.get(i).setVisibility(View.VISIBLE);
  				tv_TextView.get(i).setText(check[i]);
  			}
  			checkTfngAnswer();
  		}
  	}
    
  	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.submit_exam:
					popDialog.tip_textview.setText("您已经回答了"+(rightNum+wrongNum)+"题（共"+count+"题），考试得"+rightNum+"分，确定交卷？");
                	popDialog.popupWindow.showAtLocation(btn_next, Gravity.BOTTOM, 0, 0);
					break;
				case R.id.btn_ok:
					current = 0;
					showExamResult();
					popDialog.popupWindow.dismiss();
					break;
				case R.id.btn_cancel:
					//TODO  到达最后一题，提示是否交卷，点击取消,继续答题
					popDialog.popupWindow.dismiss();
					break;
				case R.id.answer_card:
					pwMyPopWindow.dismiss();
					break;
					//题目列表
				case R.id.tv_question_num:
					tv_rightNum.setText(""+rightNum);
					tv_wrongNum.setText(""+wrongNum);
					tv_emptyNum.setText(""+(emptyNum-rightNum-wrongNum));
					pwMyPopWindow.showAtLocation(btn_next, Gravity.BOTTOM, 0, 0);
					break;
				case R.id.submitAs:
					QuestionInfo qfo = mList.get(current);
					for(int i =0;i<li_size;i++){
						li_LinearLayout.get(i).setClickable(false);
					}
					for(int i=0;i<li_size;i++){
						tv_TextView.get(i).setTextColor(Color.BLACK);
					}
					int asSize = qfo.answerItem.size();
					if(PublicUtils.isSetEqual(qfo.selectAnswer, qfo.answer)){
						//the answer is right 
						DebugLog.i("the answer is right ");
						mList.get(current).wrongModel = 1;
						rightNum++;
						onClick(btn_next);
					}else{
						mList.get(current).wrongModel = 0;
						wrongNum++;
						for(String mc:qfo.answer){
							for(int i=0;i<asSize;i++){
								if(ci_TextView.get(i).getText().toString().equals(mc)){
									ci_TextView.get(i).setTextColor(Color.RED);
									tv_TextView.get(i).setTextColor(Color.GREEN);
								}
							}
						}
						for(String mc:qfo.selectAnswer){
							if(qfo.answer.contains(mc)){
								for(int i=0;i < asSize; i++){
									if(ci_TextView.get(i).getText().toString().equals(mc)){
										ci_TextView.get(i).setTextColor(Color.GREEN);
									}
								}
							}else{
								for(int i=0;i < asSize;i++){
									if(ci_TextView.get(i).getText().toString().equals(mc)){
										ci_TextView.get(i).setTextColor(Color.RED);
										tv_TextView.get(i).setTextColor(Color.RED);
									}
								}
							}
						}
						onClick(btn_next);
					}
					break;
				case R.id.btn_next:
					// next question
					if (current < count - 1) {
	                    current++;
	                    tv_questionNum.setText((current+1)+"/"+count);
	                    QuestionInfo qf = mList.get(current);
	                    ArrayList<AnswerInfo> af = (ArrayList<AnswerInfo>) qf.answerItem;
	    				tv_question.setText((current+1)+"."+qf.qcontent);
	    				for(int i =0;i<li_size;i++){
	    					li_LinearLayout.get(i).setVisibility(View.GONE);
	    					li_LinearLayout.get(i).setClickable(false);
	    				}
	    				int asize = af.size();
	    				// choice question 
	    				if(asize>0){
	    					for(int i =0;i < asize;i++){
	    						DebugLog.i("choice question");
	    						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
	    						li_LinearLayout.get(i).setClickable(true);
	    						li_LinearLayout.get(i).setOnClickListener(listener);
	    						tv_TextView.get(i).setText(af.get(i).itemcontent);
	    						tv_TextView.get(i).setTextColor(Color.BLACK);
	    						ci_TextView.get(i).setTextColor(Color.BLACK);
	    					}
	    					if(qf.typeid.equals("2")){
	    						DebugLog.i("mcq");
	    						if(btn_submit.getVisibility()==View.GONE){
	    							btn_submit.setVisibility(View.VISIBLE);
	    							btn_submit.setOnClickListener(listener);
	    						}
	    						img_practise.setImageResource(R.drawable.practise_duoxuanti_day);
	    						// is mcq
	    						if(qf.selectAnswer.size()!=0 && qf.wrongModel!=3){
	    							checkMcqAnswer();
	    						}
	    						if(qf.selectAnswer.size()!=0 && qf.wrongModel==3){
	    							qf.selectAnswer.clear();
	    						}
	    					}
	    					if(qf.typeid.equals("1")){
	    						DebugLog.i("scq");
	    						btn_submit.setVisibility(View.GONE);
	    						img_practise.setImageResource(R.drawable.practise_danxuanti_day);
	    						if(qf.selectAnswer.size()!=0){
	    							checkAnswer();
	    						}
	    					}
	    				}else{
	    					btn_submit.setVisibility(View.GONE);
	    					img_practise.setImageResource(R.drawable.practise_panduanti_day);
	    					for(int i =0;i < 2;i++){
	    						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
	    						li_LinearLayout.get(i).setClickable(true);
	    						li_LinearLayout.get(i).setOnClickListener(listener);
	    						tv_TextView.get(i).setText(check[i]);
	    						tv_TextView.get(i).setTextColor(Color.BLACK);
	    						ci_TextView.get(i).setTextColor(Color.BLACK);
	    					}
	    					if(qf.selectAnswer.size()!=0){
	    						DebugLog.i("tfq");
	    						checkTfngAnswer();
    						}
	    				}
	    				showExamResult();
	                }else if(current == (count - 1)){
	                	popDialog.popupWindow.showAtLocation(btn_next, Gravity.BOTTOM, 0, 0);
	                }
					break;
				case R.id.btn_previous:
					// previoue question 
					if (current > 0) {
						tv_questionNum.setText((current)+"/"+count);//top right corner show the question number
	                    current--;
	                    QuestionInfo qf = mList.get(current);
	                    ArrayList<AnswerInfo> af = (ArrayList<AnswerInfo>) qf.answerItem;
	    				tv_question.setText((current+1)+"."+qf.qcontent);
	    				for(int i =0;i<li_size;i++){
	    					li_LinearLayout.get(i).setVisibility(View.GONE);
	    					li_LinearLayout.get(i).setClickable(false);
	    				}
	    				int asize = af.size();
	    				// choice question 
	    				if(asize>0){
	    					for(int i =0;i < asize;i++){
	    						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
	    						li_LinearLayout.get(i).setClickable(true);
	    						li_LinearLayout.get(i).setOnClickListener(listener);
	    						tv_TextView.get(i).setText(af.get(i).itemcontent);
	    						tv_TextView.get(i).setTextColor(Color.BLACK);
	    						ci_TextView.get(i).setTextColor(Color.BLACK);
	    					}
	    					// check the typeid ,mcq or scq or tfq 
	    					if(qf.typeid.equals("2")){
	    						if(btn_submit.getVisibility()==View.GONE){
	    							btn_submit.setVisibility(View.VISIBLE);
	    							btn_submit.setOnClickListener(listener);
	    						}
	    						img_practise.setImageResource(R.drawable.practise_duoxuanti_day);
	    						//if has done 
	    						if(qf.selectAnswer.size()!=0 && qf.wrongModel!=3 ){
	    							checkMcqAnswer();
	    						}
	    						// if has choiced answer but hasn't submit 
	    						if(qf.selectAnswer.size()!=0 && qf.wrongModel==3){
	    							qf.selectAnswer.clear();
	    						}
	    					}
	    					if(qf.typeid.equals("1")){
	    						btn_submit.setVisibility(View.GONE);
	    						img_practise.setImageResource(R.drawable.practise_danxuanti_day);
	    						if(qf.selectAnswer.size()!=0){
	    							checkAnswer();
	    						}
	    					}
	    				}else{
	    					btn_submit.setVisibility(View.GONE);
	    					img_practise.setImageResource(R.drawable.practise_panduanti_day);
	    					for(int i =0;i < 2;i++){
	    						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
	    						li_LinearLayout.get(i).setClickable(true);
	    						li_LinearLayout.get(i).setOnClickListener(listener);
	    						tv_TextView.get(i).setText(check[i]);
	    						tv_TextView.get(i).setTextColor(Color.BLACK);
	    						ci_TextView.get(i).setTextColor(Color.BLACK);
	    					}
	    					if(qf.selectAnswer.size()!=0){
	    						DebugLog.i("tfq");
	    						checkTfngAnswer();
    						}
	    				}
	    				showExamResult();
	                }
					break;
				default:
					break;
			}
		}
	};
	
	/**check the mcq*/
	private void checkMcqAnswer(){
		for(int i =0;i<li_size;i++){
			li_LinearLayout.get(i).setClickable(false);
		}
		QuestionInfo qf =  mList.get(current);
		int asSize = mList.get(current).answerItem.size();
		if(PublicUtils.isSetEqual(qf.selectAnswer, qf.answer)){
			for(String mc:qf.selectAnswer){
				for(int i=0;i<asSize;i++){
					if(ci_TextView.get(i).getText().toString().equals(mc)){
						ci_TextView.get(i).setTextColor(Color.GREEN);
						tv_TextView.get(i).setTextColor(Color.GREEN);
					}
				}
			}
		}else if(qf.selectAnswer!=null){
			for(String mc:qf.selectAnswer){
				for(int i=0;i<asSize;i++){
					if(ci_TextView.get(i).getText().toString().equals(mc)){
						ci_TextView.get(i).setTextColor(Color.RED);
						tv_TextView.get(i).setTextColor(Color.GREEN);
					}
				}
			}
			for(String mc:qf.answer){
				if(qf.selectAnswer.contains(mc)){
					for(int i=0;i < asSize; i++){
						if(ci_TextView.get(i).getText().toString().equals(mc)){
							ci_TextView.get(i).setTextColor(Color.GREEN);
						}
					}
				}else{
					for(int i=0;i < asSize;i++){
						if(ci_TextView.get(i).getText().toString().equals(mc)){
							ci_TextView.get(i).setTextColor(Color.RED);
							tv_TextView.get(i).setTextColor(Color.GREEN);
						}
					}
				}
			}
		}else{
			for(String mc:qf.answer){
				for(int i=0;i < asSize;i++){
					if(ci_TextView.get(i).getText().toString().equals(mc)){
						ci_TextView.get(i).setTextColor(Color.RED);
						tv_TextView.get(i).setTextColor(Color.GREEN);
					}
				}
			}
		}
	}
		
	/**check the scq*/
	private void checkAnswer(){
		for(int i =0;i<li_size;i++){
			li_LinearLayout.get(i).setClickable(false);
		}
		QuestionInfo qf =  mList.get(current);
		//如果答案相同
		if(PublicUtils.isSetEqual(qf.selectAnswer, qf.answer)){
			for(int i=0;i<li_size;i++){
				if(qf.selectAnswer.contains(ci_TextView.get(i).getText().toString())){
					DebugLog.i("select answer == model answer");
					ci_TextView.get(i).setTextColor(Color.GREEN);
					tv_TextView.get(i).setTextColor(Color.GREEN);
				}
			}
		}
		//如果答案不相同
		else{
			for(int i=0;i<li_size;i++){
				if(qf.answer.contains(ci_TextView.get(i).getText().toString())){
					DebugLog.i("model answer");
					ci_TextView.get(i).setTextColor(Color.RED);
					tv_TextView.get(i).setTextColor(Color.GREEN);
				}
				if(qf.selectAnswer.contains(ci_TextView.get(i).getText().toString())){
					DebugLog.i("select answer");
					ci_TextView.get(i).setTextColor(Color.RED);
					tv_TextView.get(i).setTextColor(Color.RED);
				}
			}
		}
	}
		
	/**check the tfq*/
	private void checkTfngAnswer(){
		for(int i =0;i<li_size;i++){
			li_LinearLayout.get(i).setClickable(false);
		}
		QuestionInfo qf = mList.get(current);
		if(PublicUtils.isSetEqual(qf.answer, qf.selectAnswer)){
			DebugLog.i("model answer");
			if(qf.selectAnswer.contains("1")){
				ci_TextView.get(0).setTextColor(Color.GREEN);
				tv_TextView.get(0).setTextColor(Color.GREEN);
			}else{
				ci_TextView.get(1).setTextColor(Color.GREEN);
				tv_TextView.get(1).setTextColor(Color.GREEN);
			}
		}else{
			if(qf.selectAnswer.contains("1")){
				DebugLog.i("select answer");
				ci_TextView.get(0).setTextColor(Color.RED);
				tv_TextView.get(0).setTextColor(Color.RED);
				DebugLog.i("model answer");
				ci_TextView.get(1).setTextColor(Color.RED);
				tv_TextView.get(1).setTextColor(Color.GREEN);
			}else if(qf.selectAnswer.contains("0")){
				DebugLog.i("model answer");
				ci_TextView.get(0).setTextColor(Color.RED);
				tv_TextView.get(0).setTextColor(Color.GREEN);
				DebugLog.i("select answer");
				ci_TextView.get(1).setTextColor(Color.RED);
				tv_TextView.get(1).setTextColor(Color.RED);
			}else{
				if(qf.answer.contains("1")){
					ci_TextView.get(0).setTextColor(Color.RED);
					tv_TextView.get(0).setTextColor(Color.GREEN);
				}else{
					ci_TextView.get(1).setTextColor(Color.RED);
					tv_TextView.get(1).setTextColor(Color.GREEN);
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void changeColor(int id){
		QuestionInfo qf = mList.get(current);
		int answerSize = qf.answerItem.size();
		switch (Integer.parseInt(qf.typeid)) {
			case 1:
				//  single choice
				DebugLog.i("--single choice--"+answerSize);
				for(int i=0;i<answerSize;i++){
					ci_TextView.get(i).setTextColor(Color.BLACK);
					tv_TextView.get(i).setTextColor(Color.BLACK);
				}
				for(int i=0;i<answerSize;i++){
					//check the click 
					if(id==li_LinearLayout.get(i).getId()){
						String sAnswer = ci_TextView.get(i).getText().toString();
						qf.selectAnswer.add(sAnswer);
						DebugLog.i("--single choice--select answer--"+sAnswer);
					}
				}
				if(PublicUtils.isSetEqual(qf.answer, qf.selectAnswer)){
					DebugLog.i("--single choice--answer right");
					qf.wrongModel=1;
					rightNum++;
					// SET RIGHT COLOR
					for(int j=0;j<answerSize;j++){
						li_LinearLayout.get(j).setClickable(false);
						if(qf.answer.contains(ci_TextView.get(j).getText().toString())){
							DebugLog.i("--single choice--show the ture answer--"+ci_TextView.get(j).getText().toString());
							ci_TextView.get(j).setTextColor(Color.GREEN);
							tv_TextView.get(j).setTextColor(Color.GREEN);
						}
					}
					btn_next.performClick();
				}else{
					qf.wrongModel=0;
					wrongNum++;
					DebugLog.i("--single choice--answer wrong--"+answerSize);
					for(int j=0;j<answerSize;j++){
						li_LinearLayout.get(j).setClickable(false);
						if(qf.answer.contains(ci_TextView.get(j).getText().toString())){
							DebugLog.i("--single choice--show the ture answer--"+ci_TextView.get(j).getText().toString());
							ci_TextView.get(j).setTextColor(Color.RED);
							tv_TextView.get(j).setTextColor(Color.GREEN);
						}
						if(qf.selectAnswer.contains(ci_TextView.get(j).getText().toString())){
							DebugLog.i("--single choice--show select answer");
							ci_TextView.get(j).setTextColor(Color.RED);
							tv_TextView.get(j).setTextColor(Color.RED);
						}
					}
					btn_next.performClick();
				}
				break;
			case 2:
				 for(int i=0;i<li_size;i++){
					 //check the click is has clicked or not
					 if(id == li_LinearLayout.get(i).getId()){
						 String as = ci_TextView.get(i).getText().toString();
						 if(qf.selectAnswer.contains(as)){
							ci_TextView.get(i).setTextColor(Color.BLACK);
							tv_TextView.get(i).setTextColor(Color.BLACK);
							qf.selectAnswer.remove(as);
						 }else{
							ci_TextView.get(i).setTextColor(Color.GREEN);
							tv_TextView.get(i).setTextColor(Color.GREEN);
							qf.selectAnswer.add(as);
						 }
					 }
				 }
				break;
			case 3:
				// true or false question
				for(int i=0;i<2;i++){
					li_LinearLayout.get(i).setClickable(false);
					ci_TextView.get(i).setTextColor(Color.BLACK);
					tv_TextView.get(i).setTextColor(Color.BLACK);
					if(id==li_LinearLayout.get(i).getId()){
						String answer = ci_TextView.get(i).getText().toString();
						if(answer.equals("A")){
							answer ="1";
						
						}else{
							answer ="0";
						}
						qf.selectAnswer.add(answer);
					}
				}
				if(PublicUtils.isSetEqual(qf.answer, qf.selectAnswer)){
					qf.wrongModel=1;
					rightNum++;
					if(qf.selectAnswer.contains("1")){
						ci_TextView.get(0).setTextColor(Color.GREEN);
						tv_TextView.get(0).setTextColor(Color.GREEN);
					}else{
						ci_TextView.get(1).setTextColor(Color.GREEN);
						tv_TextView.get(1).setTextColor(Color.GREEN);
					}
					btn_next.performClick();//answer is right ,turn to next 
				}else{
					qf.wrongModel=0;
					wrongNum++;
					if(qf.selectAnswer.contains("1")){
						ci_TextView.get(0).setTextColor(Color.RED);
						tv_TextView.get(0).setTextColor(Color.RED);
						
						ci_TextView.get(1).setTextColor(Color.RED);
						tv_TextView.get(1).setTextColor(Color.GREEN);
					}else{
						ci_TextView.get(0).setTextColor(Color.RED);
						tv_TextView.get(0).setTextColor(Color.GREEN);
						
						ci_TextView.get(1).setTextColor(Color.RED);
						tv_TextView.get(1).setTextColor(Color.RED);
					}
					btn_next.performClick();
				}
				break;
			default:
				break;
		}
	}
    
    Handler handler = new Handler() {
    	@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
    		if(msg.what==3){
    			if(m_Dialog!=null){
    				m_Dialog.dismiss();
    			}
    			//开始计时
    		}
    		if (msg.what == 1) {
				if (m_Dialog != null){
					m_Dialog.dismiss();
				}
				
				mList.clear();
				mList.addAll((ArrayList<QuestionInfo>) msg.obj);
				emptyNum = count  = mList.size();
					
				QuestionInfo qf = mList.get(current);
				ArrayList<AnswerInfo> af = (ArrayList<AnswerInfo>) qf.answerItem;
				tv_question.setText((current+1)+"."+qf.qcontent);
				//
				for(int i =0;i<li_size;i++){
					li_LinearLayout.get(i).setVisibility(View.GONE);
				}
				int asize = af.size();
				
				if(asize>0){
					for(int i =0;i < asize;i++){
						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
						tv_TextView.get(i).setText(af.get(i).itemcontent.toString());
					}
					if(qf.typeid.equals("1")){
						// single choice
						btn_submit.setVisibility(View.GONE);
						img_practise.setImageResource(R.drawable.practise_danxuanti_day);
					}
					if(qf.typeid.equals("2")){
						// mcq == multiple choice question 
						btn_submit.setVisibility(View.VISIBLE);
						img_practise.setImageResource(R.drawable.practise_duoxuanti_day);
					}
				}else{
					btn_submit.setVisibility(View.GONE);
					img_practise.setImageResource(R.drawable.practise_panduanti_day);
					for(int i =0;i < 2;i++){
						li_LinearLayout.get(i).setVisibility(View.VISIBLE);
						tv_TextView.get(i).setText(check[i]);
					}
				}
				tv_questionNum.setText((current+1)+"/"+count);
				for(int i =0;i<li_size;i++){
					li_LinearLayout.get(i).setClickable(true);
					li_LinearLayout.get(i).setOnClickListener(listener);
				}
				showExamResult();
			}
			if(msg.what==2){
				if (m_Dialog != null){
					m_Dialog.dismiss();
				}
				Intent intent = new Intent(CheckExamActivity.this,MainActivity.class);
				setResult(1026, intent);
				finish();
			}
    	}
    };
    
    private class ExamGridAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		  
        public ExamGridAdapter(Context context) {  
        	mInflater = LayoutInflater.from(context);
        }  
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public QuestionInfo getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Button btn_grid;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.activity_gridview_item, null);
				btn_grid = (Button) convertView.findViewById(R.id.grid_btn);
				convertView.setTag(btn_grid);
			}else{
				btn_grid = (Button) convertView.getTag();
			}
			
			btn_grid.setText("" + (position+1) );
			if(getItem(position).wrongModel==1){
				btn_grid.setTextColor(Color.GREEN);
			}else if(getItem(position).wrongModel==0){
				btn_grid.setTextColor(Color.RED);
			}else{
				btn_grid.setTextColor(Color.GRAY);
			}
			btn_grid.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					current = Integer.parseInt(btn_grid.getText().toString())-1;
					showExamResult();
					pwMyPopWindow.dismiss();
				}
			});
			return convertView;
		}
	}
}
