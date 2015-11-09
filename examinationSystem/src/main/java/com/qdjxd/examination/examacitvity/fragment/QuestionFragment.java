package com.qdjxd.examination.examacitvity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.examacitvity.adapter.AnswerListAdapter;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.R;
import com.qdjxd.examination.views.CircleTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by asus on 2015/11/05.
 */
public class QuestionFragment extends ListFragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();
    private View questionView;

    ArrayAdapter<AnswerInfo> adapter;
    private QuestionInfo questionInfo;
    private List<AnswerInfo> answerItem;
    private final List<String> list = Arrays.asList("正确","错误");
    public QuestionFragment(QuestionInfo qf){
        this.questionInfo = qf;
        this.answerItem = qf.answerItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.i("onCreate");
        DebugLog.i(TAG);
        Activity activity = getActivity();
        if(answerItem.size()==0){
            for(int i=0;i<list.size();i++) {
                AnswerInfo answerInfo = new AnswerInfo();
                answerInfo.itemcontent = list.get(i);
                switch (i){
                    case 0:
                        answerInfo.itemvalue ="A";
                        break;
                    case 1:
                        answerInfo.itemvalue ="B";
                        break;
                }

                answerItem.add(answerInfo);
            }
        }
        adapter = new AnswerListAdapter(activity,answerItem);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //问题显示页面
        questionView = inflater.inflate(R.layout.activity_exam_autonomous_item,null);
        //题目内容
        TextView questionTx = (TextView) questionView.findViewById(R.id.choose_question_content);
        questionTx.setText(questionInfo.qcontent);
        DebugLog.i(questionInfo.qcontent);
        //对题目类型进行判断,更改题目类型图标
        if(questionInfo.typeid.equals("1")){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_danxuanti_day), null, null, null);
        }else if(questionInfo.typeid.equals("2")){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_duoxuanti_day), null, null, null);
        }else if (questionInfo.typeid.equals("3")){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_panduanti_day), null, null, null);
        }
        setListAdapter(adapter);
        getListView().addHeaderView(questionView);
    }
}
