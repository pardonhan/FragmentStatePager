package com.qdjxd.examination.examacitvity.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.AutonomousExamActivity;
import com.qdjxd.examination.examacitvity.adapter.AnswerListAdapter;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.DebugLog;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by asus on 2015/11/05.
 *
 */
public class QuestionFragment extends ListFragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();
    private View questionView;
    private View buttonView;
    private AnswerListAdapter adapter;
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
        adapter = new AnswerListAdapter(activity,questionInfo);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //问题显示页面
        questionView = inflater.inflate(R.layout.activity_exam_autonomous_item,null);
        buttonView = inflater.inflate(R.layout.activity_exam_answer_button,null);
        Button button = (Button) buttonView.findViewById(R.id.submitAs);
        //题目内容
        TextView questionTx = (TextView) questionView.findViewById(R.id.choose_question_content);
        questionTx.setText(num + "." + questionInfo.qcontent);

        //对题目类型进行判断,更改题目类型图标 1 单选，2 多选 3 判断
        if(("1").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_danxuanti_day), null, null, null);
        }else if(("2").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_duoxuanti_day), null, null, null);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(listener);
        }else if (("3").equals(questionInfo.typeid)){
            questionTx.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.practise_panduanti_day), null, null, null);
        }

        //listView.setAdapter(adapter);
        setListAdapter(adapter);

        getListView().addHeaderView(questionView);
        getListView().addFooterView(buttonView);
        getListView().setCacheColorHint(0);
        getListView().setDividerHeight(0);
        getListView().setSelector(R.color.white);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DebugLog.i("onListItemClick" + position);
                TextView tx = (TextView) view.findViewById(R.id.item_answer);
                int length = select.length;
                if(!(("2").equals(questionInfo.typeid))) {
                    //非多选题，进行答题时的逻辑
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
                        //如果之前被选中（select[i]是true）,但不是当前点击项
                        if ((i + 1) != position && select[i]) {
                            //DebugLog.i("点击其他项目，取消之前选择");
                           // setTextView(tx, position, select[i]);
                            select[i] = false;
                        }
                        // DebugLog.i(select[i]);
                    }
                }else{
                    // 多选题

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(questionInfo.selectAnswer!=null) {
                            adapter.questionInfo = questionInfo;
                            adapter.notifyDataSetChanged();

                        }
                    }
                },400);
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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
        tx.setCompoundDrawablesWithIntrinsicBounds(getActivity().getResources().getDrawable(id), null, null, null);
    }

}
