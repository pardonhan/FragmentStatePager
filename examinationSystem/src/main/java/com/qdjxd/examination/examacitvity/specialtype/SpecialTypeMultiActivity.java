package com.qdjxd.examination.examacitvity.specialtype;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.baseInfo.CurrentInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.examacitvity.fragment.QuestionFragment;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.MsgUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2015/11/30.
 * 专项练习--
 *      多项选择模块
 */
public class SpecialTypeMultiActivity extends BaseActivity {
    private final String type_id = "2";
    private final String exam_type = "3";
    private CurrentInfo ci = new CurrentInfo();
    private TextView tv_questionNum;
    private List<QuestionFragment> questionFragmentList;
    private List<QuestionInfo> questionInfoList = new ArrayList<QuestionInfo>();
    private ViewPager mViewPager;
    private AutoNomousAdapter adapter;
    private Dialog m_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_autonomous);
        //返回按钮
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getExamData();
        tv_questionNum = (TextView) findViewById(R.id.tv_question_num);
        tv_questionNum.setOnClickListener(listener);
        FragmentManager manager = getFragmentManager();
        questionFragmentList = new ArrayList<QuestionFragment>();
        if (questionInfoList != null) {
            int size = questionInfoList.size();
            for (int i = 0; i < size; i++) {
                questionFragmentList.add(QuestionFragment.newInstance(questionInfoList.get(i), (i + 1) + "",type_id,exam_type));
            }
        }
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new AutoNomousAdapter(manager, questionFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(pListener);
    }

    ViewPager.OnPageChangeListener pListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            tv_questionNum.setText((mViewPager.getCurrentItem() + 1) + "/" + questionInfoList.size());
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private void getExamData() {
        if(m_Dialog!=null){
            m_Dialog.show();
        }else {
            m_Dialog = MsgUtil.ShowLoadDialog(SpecialTypeMultiActivity.this, "请稍后","正在加载数据...");
        }
        new Thread() {
            @Override
            public void run() {
                DebugLog.i("查询数据");
                ArrayList<QuestionInfo> _List;
                _List = DataBaseUtils.getRandomQuestionInfo(SpecialTypeMultiActivity.this, type_id, exam_type);
                if (_List.size() > 0) {
                    handler.sendMessage(handler.obtainMessage(1, _List));
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (m_Dialog != null) {
                        m_Dialog.dismiss();
                    }
                    questionInfoList.clear();
                    questionInfoList.addAll((ArrayList<QuestionInfo>) msg.obj);
                    if (questionInfoList != null) {
                        int size = questionInfoList.size();
                        for (int i = 0; i < size; i++) {
                            questionFragmentList.add(QuestionFragment.newInstance(questionInfoList.get(i), (i + 1) + "",type_id,exam_type));
                        }
                    }
                    adapter.notifyDataSetChanged();
                    mViewPager.setCurrentItem(0);
                    tv_questionNum.setText((mViewPager.getCurrentItem() + 1) + "/" + questionInfoList.size());
                    break;
                case 2:
                    //TODO 如果没有多选题
                    break;
            }
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_question_num:

                    break;
            }

        }
    };
    private class AutoNomousAdapter extends FragmentStatePagerAdapter {
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
}
