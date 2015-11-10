package com.qdjxd.examination.examacitvity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DebugLog;

import java.util.Arrays;
import java.util.List;

/**
 * Created by asus on 2015/11/09.
 */
public class QuestionFragments extends Fragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();

    private QuestionInfo questionInfo;
    private List<AnswerInfo> answerItem;

    private Activity activity;
    private final List<String> list = Arrays.asList("正确", "错误");
    public QuestionFragments(QuestionInfo qf){
        this.questionInfo = qf;
        this.answerItem = qf.answerItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_exam_autonomous_item,null);
        //题目内容
        TextView questionTx = (TextView) view.findViewById(R.id.choose_question_content);
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
