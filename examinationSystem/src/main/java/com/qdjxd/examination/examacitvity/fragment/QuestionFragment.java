package com.qdjxd.examination.examacitvity.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.adapter.AnswerListAdapter;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DebugLog;

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
    private String num;
    private boolean[] select;
    public QuestionFragment(QuestionInfo qf,String num){
        this.questionInfo = qf;
        this.answerItem = qf.answerItem;
        this.num = num;
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
        questionTx.setText(num+"."+questionInfo.qcontent);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        DebugLog.i("onListItemClick" + position);
        TextView tx = (TextView) v.findViewById(R.id.item_answer);
        for(int i=0;i<select.length;i++){
            //对当前点击项目做出判断
            if((i+1)==position){
                if(select[i]){
                    DebugLog.i("点击当前选中项，取消选择");
                    setTextView(tx, position, select[i]);
                    select[i] = false;
                }else{
                    DebugLog.i("初次点击选中");
                    setTextView(tx,position,select[i]);
                    select[i] = true;
                }
            }
            //如果之前被选中（select[i]是true）,但不是当前点击项
            if((i+1)!=position&&select[i]){
                DebugLog.i("点击其他项目，取消之前选择");
                setTextView(tx,position,select[i]);
                select[i] = false;
            }
            DebugLog.i(select[i]);
        }

    }
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
        DebugLog.i("setTextViewDrawable");
        tx.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(id), null, null, null);
    }
}
