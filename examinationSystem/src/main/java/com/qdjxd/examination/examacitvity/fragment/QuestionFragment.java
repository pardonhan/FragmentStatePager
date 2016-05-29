package com.qdjxd.examination.examacitvity.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.baseInfo.CurrentInfo;
import com.qdjxd.examination.examacitvity.adapter.AnswerListAdapter;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.PublicUtils;
import com.qdjxd.examination.utils.SharedPreferencesHelper;

import java.util.Arrays;
import java.util.List;

/**
 * Created by asus on 2015/11/05.
 *
 */
public class QuestionFragment extends ListFragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();

    private AnswerListAdapter adapter;
    private QuestionInfo questionInfo;
    private List<AnswerInfo> answerItem;
    private final List<String> list = Arrays.asList("正确","错误");
    private String num;
    private boolean[] select;
    private String type_id;
    private String exam_type;

    public QuestionFragment(QuestionInfo qf,String num,String type_id,String exam_type){
        this.questionInfo = qf;
        this.answerItem = qf.answerItem;
        this.num = num;
        this.type_id = type_id;
        this.exam_type = exam_type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugLog.i(TAG);
        Activity activity = getActivity();
        select = new boolean[]{false,false,false,false,false};
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
        adapter = new AnswerListAdapter(activity,questionInfo);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //问题显示页面
        View questionView = inflater.inflate(R.layout.activity_exam_autonomous_item, null);
        View buttonView = inflater.inflate(R.layout.activity_exam_answer_button, null);
        Button button = (Button) buttonView.findViewById(R.id.submitAs);
        //题目内容
        TextView questionTx = (TextView) questionView.findViewById(R.id.choose_question_content);
        String text = num + "." + questionInfo.qcontent;
        questionTx.setText(text);

        //对题目类型进行判断,更改题目类型图标 1 单选，2 多选 3 判断
        if(("1").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getActivity(),R.drawable.practise_danxuanti_day), null, null, null);
        }else if(("2").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getActivity(),R.drawable.practise_duoxuanti_day), null, null, null);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(listener);
        }else if (("3").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(getActivity(),R.drawable.practise_panduanti_day), null, null, null);
        }
        setListAdapter(adapter);
        getListView().addHeaderView(questionView);
        getListView().addFooterView(buttonView);
        getListView().setCacheColorHint(0);
        getListView().setDividerHeight(0);
        getListView().setSelector(R.color.white);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tx = (TextView) view.findViewById(R.id.item_answer);
                int length = select.length;
                if(!(("2").equals(questionInfo.typeid))) {
                    DebugLog.i("questionInfo.wrongModel="+questionInfo.wrongModel);
                    //更改点击项
                    if(questionInfo.wrongModel==3){
                        setTextView(tx, position, select[position-1]);
                        select[position-1] = true;
                        //记录选项
                        for (int i = 0; i < length; ++i) {
                            //DebugLog.i("记录选项");
                            String ans = ((char) (65 + i)) + "";
                            if (select[i] && !questionInfo.selectAnswer.contains(ans)) {
                                questionInfo.selectAnswer.add(ans);
                            } else if (!select[i] && questionInfo.selectAnswer.contains(ans)) {
                                questionInfo.selectAnswer.remove(ans);
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (questionInfo.selectAnswer.size()>0) {
                                    if(questionInfo.selectAnswer.size()>0) {
                                        if (PublicUtils.isSetEqual(questionInfo.selectAnswer, questionInfo.answer)) {
                                            questionInfo.wrongModel = 1;
                                        } else {
                                            questionInfo.wrongModel = 0;
                                        }
                                    }
                                    adapter.questionInfo = questionInfo;
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }, 400);
                    }
                }else{
                    // 多选题
                    if(questionInfo.wrongModel==3){
                        for (int i = 0; i < length; i++) {
                            //对当前点击项目做出判断
                            if ((i + 1) == position) {
                                if (select[i]) {
                                    //DebugLog.i("点击当前选中项，取消选择");
                                    setTextView(tx, position, select[i]);
                                    select[i] = false;
                                } else {
                                    //DebugLog.i("初次点击选中");
                                    setTextView(tx, position, select[i]);
                                    select[i] = true;
                                }
                            }
                            DebugLog.i(select[i]);
                        }
                        //记录选项
                        for (int i = 0; i < length; ++i) {
                            //DebugLog.i("记录选项");
                            String ans = ((char) (65 + i)) + "";
                            if (select[i] && !questionInfo.selectAnswer.contains(ans)) {
                                questionInfo.selectAnswer.add(ans);
                            } else if (!select[i] && questionInfo.selectAnswer.contains(ans)) {
                                questionInfo.selectAnswer.remove(ans);
                            }
                        }
                    }
                }
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.submitAs:
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DebugLog.i("listener");
                            if (questionInfo.selectAnswer.size()>0) {
                                if(questionInfo.selectAnswer.size()>0) {
                                    if (PublicUtils.isSetEqual(questionInfo.selectAnswer, questionInfo.answer)) {
                                        questionInfo.wrongModel = 1;
                                    } else {
                                        questionInfo.wrongModel = 0;
                                    }
                                }
                                adapter.questionInfo = questionInfo;
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }, 400);
                    break;
            }

        }
    };
    @Override
    public void onPause() {
        super.onPause();
        DebugLog.i("onPause:"+questionInfo.qcontent);
        if(questionInfo.wrongModel!=3&&questionInfo.selectAnswer.size()>0){
            CurrentInfo currentInfo = new CurrentInfo();
            SharedPreferencesHelper sp = SharedPreferencesHelper.getDefault(getActivity());
            currentInfo.setAutoExam(sp,Integer.parseInt(num));
            new Thread(){
                public void run() {
                    DataBaseUtils.saveExamResult(getActivity(),
                            questionInfo, type_id, exam_type);
                }
            }.start();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     *
     * @param tx 当前选项
     * @param position  选项标志位
     * @param flag  是否选中：flag==true执行else，flag==false执行if
     */
    private void setTextView(TextView tx,int position,boolean flag){
        switch (position){
            case 1:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_a_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_a_s_day);
                }
                break;
            case 2:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_b_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_b_s_day);
                }
                break;
            case 3:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_c_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_c_s_day);
                }
                break;
            case 4:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_d_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_d_s_day);
                }
                break;
            case 5:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_e_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_e_s_day);
                }
                break;
            case 6:
                if(flag){
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_f_n_day);
                }else{
                    setTextViewDrawable(tx,R.drawable.jiakao_practise_f_s_day);
                }
                break;
        }
    }
    private void setTextViewDrawable(TextView tx,int id){
        tx.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(getActivity(),id), null, null, null);
    }

}
