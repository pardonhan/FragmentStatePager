package com.qdjxd.examination.examacitvity.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.utils.PublicUtils;

import java.util.Set;

/**
 * Created by asus on 2015/11/09.
 *  问题选项列表
 */
public class AnswerListAdapter extends BaseAdapter {
    private Activity activity;
    public QuestionInfo questionInfo;
    private final static String A = "A";
    private final static String B = "B";
    private final static String C = "C";
    private final static String D = "D";
    private final static String E = "E";
    private final static String F = "F";
    //private
    public AnswerListAdapter(Activity activity,QuestionInfo questionInfo){
        this.activity = activity;
        this.questionInfo = questionInfo;

    }


    @Override
    public int getCount() {
        return questionInfo.answerItem.size();
    }

    @Override
    public AnswerInfo getItem(int position) {
        return questionInfo.answerItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewWrapper viewWrapper =null;
        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.activity_exam_answer_list_item,null);
            viewWrapper = new ViewWrapper(view);
            view.setTag(viewWrapper);
        }else{
            viewWrapper = (ViewWrapper) view.getTag();
        }

        AnswerInfo answerInfo = questionInfo.answerItem.get(position);
        //DebugLog.i(answerInfo.itemcontent);
        //DebugLog.i("***"+viewWrapper.text);
        viewWrapper.text.setText(answerInfo.itemcontent);//选项内容

        if (answerInfo.itemvalue.contains(A)) {
            //如果所选答案如当前选项数量一样
            if (questionInfo.selectAnswer.contains(A)) {
                //判断是否正确
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,A);
            } else {
                if(questionInfo.answer.contains(A)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,A);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_a_n_day), null, null, null);
                }
            }
        } else if (answerInfo.itemvalue.contains(B)) {
            if (questionInfo.selectAnswer.contains(B)) {
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,B);
            } else {
                if(questionInfo.answer.contains(B)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,B);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_b_n_day), null, null, null);
                }
            }
        } else if (answerInfo.itemvalue.contains(C)) {
            if (questionInfo.selectAnswer.contains(C)) {
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,C);
            } else {
                if(questionInfo.answer.contains(C)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,C);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_c_n_day), null, null, null);
                }
            }
        } else if (answerInfo.itemvalue.contains(D)) {
            if (questionInfo.selectAnswer.contains(D)) {
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,D);
            } else {
                if(questionInfo.answer.contains(D)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,D);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_d_n_day), null, null, null);
                }
            }
        } else if (answerInfo.itemvalue.contains(E)) {
            if (questionInfo.selectAnswer.contains(E)) {
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,E);
            } else {
                if(questionInfo.answer.contains(E)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,E);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_e_n_day), null, null, null);
                }
            }
        } else if (answerInfo.itemvalue.contains(F)) {
            if (questionInfo.selectAnswer.contains(F)) {
                checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,F);
            } else {
                if(questionInfo.answer.contains(F)&&questionInfo.wrongModel!=3){
                    checkQuestion(viewWrapper.text, questionInfo.selectAnswer, questionInfo.answer,F);
                }else {
                    viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_f_n_day), null, null, null);
                }
            }
        }
        return view;
    }


    /**
     * 对选项结果进行判断
     * @param textView
     * @param selectAs
     * @param rightAs
     */
    private void checkQuestion(TextView textView,Set<String> selectAs,Set<String> rightAs,String check){
        if (rightAs.contains(check)) {
            if (selectAs.contains(check)) {
                textView.setCompoundDrawablesWithIntrinsicBounds(
                        activity.getResources().getDrawable(R.drawable.jiakao_practise_true_day), null, null, null);
                textView.setTextColor(activity.getResources().getColor(R.color.rightAnswer));
            } else {
                if(questionInfo.typeid.equals("2")){
                    //没有选择该选项
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_true_day), null, null, null);
                    textView.setTextColor(activity.getResources().getColor(R.color.wrongAnswer));
                }else {
                    //没有选择该选项
                    textView.setCompoundDrawablesWithIntrinsicBounds(
                            activity.getResources().getDrawable(R.drawable.jiakao_practise_true_day), null, null, null);
                    textView.setTextColor(activity.getResources().getColor(R.color.rightAnswer));
                }
            }
        } else {
            //该选项不是正确选项
            if (selectAs.contains(check)) {
                //但是选择了该选项
                textView.setCompoundDrawablesWithIntrinsicBounds(
                        activity.getResources().getDrawable(R.drawable.jiakao_practise_false_day), null, null, null);
                textView.setTextColor(activity.getResources().getColor(R.color.wrongAnswer));
            }
        }
    }
    class ViewWrapper {
        public final TextView text;
        ViewWrapper(View view){
            this.text = (TextView)view.findViewById(R.id.item_answer);
        }
    }
}
