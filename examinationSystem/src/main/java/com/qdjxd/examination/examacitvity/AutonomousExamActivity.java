package com.qdjxd.examination.examacitvity;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.baseInfo.CurrentInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.examacitvity.fragment.QuestionFragment;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.PopupWindowDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * 自主答题主页面--AutonomousExamActivity
 *
 */
public class AutonomousExamActivity extends BaseActivity {
    private final String type_id = "0";
    private final String exam_id = "4";
    private CurrentInfo ci = new CurrentInfo();
    private ViewPager mViewPager;
    private AutoNomousAdapter adapter;
    private List<QuestionInfo> questionInfoList = new ArrayList<>();
    private List<QuestionInfo> questionInfoList1 = new ArrayList<>();
    private List<QuestionFragment> questionFragmentList;
    private TextView tv_questionNum;
    private PopupWindow pwMyPopWindow;// popupwindow
    private PopupWindowDialog popDialog;//no next practice dialog
    private PopupWindowDialog startPopDialog;//
    private Dialog m_Dialog;
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
        initPopupWindow();
        tv_questionNum = (TextView) findViewById(R.id.tv_question_num);
        tv_questionNum.setOnClickListener(listener);
        FragmentManager manager = getFragmentManager();
        questionFragmentList = new ArrayList<>();
        if (questionInfoList != null) {
            int size = questionInfoList.size();
            for (int i = 0; i < size; i++) {
                questionFragmentList.add(QuestionFragment.newInstance(questionInfoList.get(i), (i + 1) + "",type_id,exam_id));
            }
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new AutoNomousAdapter(manager, questionFragmentList);
        mViewPager.setAdapter(adapter);
        DebugLog.i(ci.getAutoExam(sp));
        if(ci.getAutoExam(sp)!=0){
            mViewPager.setCurrentItem(ci.getAutoExam(sp));
        }else {
            mViewPager.setCurrentItem(0);
        }
        startPopDialog = new PopupWindowDialog(AutonomousExamActivity.this,"是否继续上次答题，确定继续，取消重新开始");
        startPopDialog.btn_ok.setText("继续答题");
        startPopDialog.btn_ok.setOnClickListener(listenerT);
        startPopDialog.btn_cancel.setText("重新开始");
        startPopDialog.btn_cancel.setOnClickListener(listenerT);
        getExamData("0");
        mViewPager.addOnPageChangeListener(pListener);
        //最后一题时提示
        popDialog = new PopupWindowDialog(AutonomousExamActivity.this, null);
        popDialog.btn_ok.setOnClickListener(listener);
        popDialog.btn_cancel.setOnClickListener(listener);
    }

    ViewPager.OnPageChangeListener pListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            //页面滑动时调用此方法： arg0：当前页面；arg1:滑动偏移量；arg2：滑动偏移像素
        }

        @Override
        public void onPageSelected(int i) {
            tv_questionNum.setText((mViewPager.getCurrentItem() + 1) + "/" + questionInfoList.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            //arg0 有三中状态（0 未做 .1 滑动中.2 滑动完）
            if (i == 1) {
                if ((mViewPager.getCurrentItem() + 1) == questionInfoList.size()) {
                    popDialog.popupWindow.showAtLocation(mViewPager, Gravity.BOTTOM, 0, 0);
                }
            }
        }
    };

    private void getExamData(final String type_id) {
        new Thread() {
            @Override
            public void run() {
                DebugLog.i("查询数据");
                ArrayList<QuestionInfo> _List;// = new ArrayList<QuestionInfo>();
                _List = DataBaseUtils.getRandomQuestionInfo(AutonomousExamActivity.this, type_id,"");
                if (_List.size() > 0) {
                    handler.sendMessage(handler.obtainMessage(1, _List));
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (m_Dialog != null) {
                    m_Dialog.dismiss();
                }
                questionInfoList.clear();
                questionInfoList.addAll((ArrayList<QuestionInfo>) msg.obj);
                if(ci.getAutoExam(sp)!=0) {
                    startPopDialog.popupWindow.showAtLocation(mViewPager, Gravity.BOTTOM, 0, 0);
                }else{
                    handler.sendEmptyMessage(3);
                }
            }else if(msg.what == 3){
                if (questionInfoList != null) {
                    int size = questionInfoList.size();
                    for (int i = 0; i < size; i++) {
                        questionFragmentList.add( QuestionFragment.newInstance(questionInfoList.get(i), (i + 1) + "",type_id,exam_id));
                    }
                }
                adapter.notifyDataSetChanged();
                if(ci.getAutoExam(sp)!=0){
                    mViewPager.setCurrentItem(ci.getAutoExam(sp));
                }else {
                    mViewPager.setCurrentItem(0);
                }
                tv_questionNum.setText((mViewPager.getCurrentItem() + 1) + "/" + questionInfoList.size());
            }else if(msg.what==4){
                //清楚答题信息，清除sharepreference信息
                questionInfoList1.clear();
                questionInfoList1.addAll(questionInfoList);
                for(int i= 0;i<questionInfoList.size();i++){
                    questionInfoList.get(i).wrongModel=3;
                    questionInfoList.get(i).selectAnswer.clear();
                }
                new Thread(){
                    @Override
                    public void run() {
                        DebugLog.i("删除答题信息");
                        DataBaseUtils.deleteExamResult(AutonomousExamActivity.this,questionInfoList1,"JXD7_EX_RANDOMRESULT");
                    }
                }.start();
                ci.setAutoExam(sp,0);
                if (questionInfoList != null) {
                    int size = questionInfoList.size();
                    for (int i = 0; i < size; i++) {
                        questionFragmentList.add(QuestionFragment.newInstance(questionInfoList.get(i), (i + 1) + "",type_id,exam_id));
                    }
                }
                adapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(0);
                tv_questionNum.setText((mViewPager.getCurrentItem() + 1) + "/" + questionInfoList.size());
            }
        }
    };

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ok:
                    popDialog.popupWindow.dismiss();
                    break;
                case R.id.tv_question_num:
                    pwMyPopWindow.showAtLocation(mViewPager, Gravity.BOTTOM, 0, 0);
                    break;
                case R.id.answer_card:
                    pwMyPopWindow.dismiss();
                    break;
            }
        }
    };
    OnClickListener listenerT = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //点击确定，直接继续答题
                case R.id.btn_ok:
                    handler.sendEmptyMessage(3);
                    startPopDialog.popupWindow.dismiss();
                    break;
                //点击取消重新开始
                case R.id.btn_cancel:
                    handler.sendEmptyMessage(4);
                    startPopDialog.popupWindow.dismiss();
                    break;
            }
        }
    };
    private void initPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) AutonomousExamActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_gridview_exam, null);
        RelativeLayout title_re = (RelativeLayout) layout.findViewById(R.id.title_re);
        title_re.getBackground().setAlpha(180);//set titlebar background diaphaneity
        LinearLayout li_down = (LinearLayout) layout.findViewById(R.id.answer_card);
        li_down.setOnClickListener(listener);
        GridView gv_gridview = (GridView) layout.findViewById(R.id.gview);
        pwMyPopWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pwMyPopWindow.setFocusable(true);
        pwMyPopWindow.setAnimationStyle(R.style.AnimationFade);//set Animation
        ExamGridAdapter egadapter = new ExamGridAdapter(AutonomousExamActivity.this);
        gv_gridview.setAdapter(egadapter);
    }

    private class AutoNomousAdapter extends FragmentStatePagerAdapter
		/*implements ViewPager.OnPageChangeListener*/ {
        FragmentManager fm;
        List<QuestionFragment> list = new ArrayList<QuestionFragment>();

        public AutoNomousAdapter(FragmentManager fm, List<QuestionFragment> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }

        //获取要滑动的控件数量，此处是是根据题目数量来确定
        @Override
        public int getCount() {
            return list.size();
        }
    }

    /**
     * 题目编号显示列表适配器
     */
    private class ExamGridAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ExamGridAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return questionInfoList.size();
        }

        @Override
        public QuestionInfo getItem(int position) {
            return questionInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Button btn_grid;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_gridview_item, null);
                btn_grid = (Button) convertView.findViewById(R.id.grid_btn);
                convertView.setTag(btn_grid);
            } else {
                btn_grid = (Button) convertView.getTag();
            }

            btn_grid.setText("" + (position + 1));
            if (getItem(position).wrongModel == 1) {
                btn_grid.setTextColor(Color.GREEN);
            } else if (getItem(position).wrongModel == 0) {
                btn_grid.setTextColor(Color.RED);
            } else {
                btn_grid.setTextColor(Color.GRAY);
            }
            //gridview 点击事件，跳转到对应的题目
            btn_grid.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    int num = Integer.parseInt(btn_grid.getText().toString()) - 1;
                    mViewPager.setCurrentItem(num);
                    pwMyPopWindow.dismiss();
                }
            });
            return convertView;
        }
    }
}