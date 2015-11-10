package com.qdjxd.examination.examacitvity.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;

import java.util.List;

/**
 * Created by asus on 2015/11/09.
 *  问题选项列表
 */
public class AnswerListAdapter extends ArrayAdapter<AnswerInfo> {
    private Activity activity;
    //private
    public AnswerListAdapter(Activity activity,List<AnswerInfo> answerInfoList){
        super(activity, R.layout.activity_activity_exam_answer_list_item,answerInfoList);
        this.activity =activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewWrapper viewWrapper =null;
        if(view == null){
            view = activity.getLayoutInflater().inflate(R.layout.activity_activity_exam_answer_list_item,null);
            viewWrapper = new ViewWrapper(view);
            view.setTag(viewWrapper);
        }
        AnswerInfo answerInfo = getItem(position);

        //viewWrapper.label.setText(answerInfo.itemvalue);
        viewWrapper.text.setText(answerInfo.itemcontent);

        if(answerInfo.itemvalue.equals("A")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_a_n_day), null, null, null);
        }else if(answerInfo.itemvalue.equals("B")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_b_n_day), null, null, null);
        }else if(answerInfo.itemvalue.equals("C")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_c_n_day), null, null, null);
        }else if(answerInfo.itemvalue.equals("D")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_d_n_day), null, null, null);
        }else if(answerInfo.itemvalue.equals("E")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_e_n_day), null, null, null);
        }else if(answerInfo.itemvalue.equals("F")){
            viewWrapper.text.setCompoundDrawablesWithIntrinsicBounds(
                    activity.getResources().getDrawable(R.drawable.jiakao_practise_f_n_day), null, null, null);
        }

        return view;
    }
    class ViewWrapper {
        //public final CircleTextView label;
        public final TextView text;

        ViewWrapper(View view){
            //this.label = (CircleTextView)view.findViewById(R.id.s_answer_b);
            this.text = (TextView)view.findViewById(R.id.item_answer);
        }
    }
}
