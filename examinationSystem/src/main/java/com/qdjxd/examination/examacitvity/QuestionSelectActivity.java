package com.qdjxd.examination.examacitvity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.utils.MsgUtil;
/**
 * 问题查询
 * 	在输入框输入问题关键字进行查询，
 * @author asus
 *
 */
public class QuestionSelectActivity extends BaseActivity {
	private ImageView searchStart;
	private EditText searchText;
	private ListView question_listview;
	private QuestionAdapter questionAdapter;
	private Dialog m_Dialog;
	private ArrayList<QuestionInfo> mList = new ArrayList<QuestionInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_select);
		this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		searchPanel();
		question_listview = (ListView) findViewById(R.id.question_listview);
		questionAdapter = new QuestionAdapter(QuestionSelectActivity.this);
		question_listview.setAdapter(questionAdapter);
		
	}
	private void searchPanel(){
		searchStart = (ImageView) findViewById(R.id.search);
		searchStart.setOnClickListener(listener);
		searchText = (EditText) findViewById(R.id.list_search_text);
		searchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			} 
		});
	}
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.search:
				if(searchText.getText()==null||searchText.getText().toString().equals("")){
					MsgUtil.ShowToast("请输入查询内容！", QuestionSelectActivity.this);
				}else{
					getData();
				}
				break;
			default:
				break;
			}
		}
	};
	private void getData(){
		if(m_Dialog!=null){
			m_Dialog.show();
		}else{
			m_Dialog = MsgUtil.ShowLoadDialog(QuestionSelectActivity.this, "请稍等", "正在查询...");
		}
		new Thread(){
			@Override
			public void run() {
				ArrayList<QuestionInfo> _List = new ArrayList<QuestionInfo>();
				_List = DataBaseUtils.getSelectQuestion(QuestionSelectActivity.this,searchText.getText().toString());
				handler.sendMessage(handler.obtainMessage(1, _List));
			}
		}.start();
	}
	Handler handler = new Handler(){
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				if(m_Dialog!=null){
					m_Dialog.dismiss();
				}
				mList.clear();
				mList = (ArrayList<QuestionInfo>) msg.obj;
				questionAdapter.notifyDataSetChanged();
			}
		}
	};
	class QuestionAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		public QuestionAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder holder = null;
			if(arg1==null){
				holder = new ViewHolder();
				arg1 = inflater.inflate(R.layout.listview_question_content, null);
				holder.qContent = (TextView) arg1.findViewById(R.id.qcontent);
				holder.questionType = (ImageView) arg1.findViewById(R.id.qtype);
				holder.qNumber = (TextView) arg1.findViewById(R.id.number);
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			final QuestionInfo qf = mList.get(arg0);
			holder.qContent.setText(qf.qcontent);
			holder.qNumber.setText((arg0+1)+".");
			switch (Integer.parseInt(qf.typeid)) {
				case 1:
					holder.questionType.setImageResource(R.drawable.practise_danxuanti_day);
					break;
				case 2:
					holder.questionType.setImageResource(R.drawable.practise_duoxuanti_day);
					break;
				case 3:
					holder.questionType.setImageResource(R.drawable.practise_panduanti_day);
					break;
				default:
					break;
			}
			holder.qContent.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(QuestionSelectActivity.this, ShowQuestionActivity.class);
					intent.putExtra("question", qf);
					startActivity(intent);
				}
			});
			return arg1;
		}
	}
	public static class ViewHolder {
		public ImageView questionType;
		public TextView qNumber;
		public TextView qContent;
	}
}
