package com.qdjxd.examination.examacitvity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;
import com.qdjxd.examination.views.CircleTextView;

/**
 * 查看查询结果--问题详情页面
 * 点击查询出的问题列表中的问题，进入问题详情页面
 * 此页面显示题目类型，题目内容，选项内容，正确答案，（答案解析）
 *
 * @author asus
 */
public class ShowQuestionActivity extends BaseActivity {
    private List<CircleTextView> ci_TextView;
    private List<LinearLayout> li_LinearLayout;
    private List<TextView> tv_TextView;
    private ImageView img_practise;
    private TextView tv_question;
    private int li_size;

    private String[] check = {"正确", "错误"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        this.findViewById(R.id.submit_exam).setVisibility(View.GONE);
        this.findViewById(R.id.li_question_time).setVisibility(View.GONE);
        this.findViewById(R.id.tv_question_num).setVisibility(View.GONE);
        this.findViewById(R.id.btn_next).setVisibility(View.GONE);
        this.findViewById(R.id.btn_previous).setVisibility(View.GONE);
        this.findViewById(R.id.submitAs).setVisibility(View.GONE);
        Intent intent = getIntent();
        QuestionInfo qf = (QuestionInfo) intent.getSerializableExtra("question");
        List<AnswerInfo> anList = qf.answerItem;
        initView();
        switch (Integer.parseInt(qf.typeid)) {
            case 1:
                img_practise.setImageResource(R.drawable.practise_danxuanti_day);
                break;
            case 2:
                img_practise.setImageResource(R.drawable.practise_duoxuanti_day);
                break;
            case 3:
                img_practise.setImageResource(R.drawable.practise_panduanti_day);
                break;
            default:
                break;
        }
        tv_question.setText(qf.qcontent);
        for (int i = 0; i < li_size; i++) {
            li_LinearLayout.get(i).setVisibility(View.GONE);
        }
        if (qf.typeid.equals("3")) {
            for (int i = 0; i < 2; i++) {
                li_LinearLayout.get(i).setVisibility(View.VISIBLE);
                tv_TextView.get(i).setText(check[i]);
            }
            if (qf.answer.contains("1")) {
                ci_TextView.get(0).setTextColor(Color.GREEN);
                tv_TextView.get(0).setTextColor(Color.GREEN);
            } else {
                ci_TextView.get(1).setTextColor(Color.GREEN);
                tv_TextView.get(1).setTextColor(Color.GREEN);
            }
        } else {
            for (int i = 0; i < anList.size(); i++) {
                li_LinearLayout.get(i).setVisibility(View.VISIBLE);
                tv_TextView.get(i).setText(anList.get(i).itemcontent);
                if (qf.answer.contains(ci_TextView.get(i).getText().toString())) {
                    tv_TextView.get(i).setTextColor(Color.GREEN);
                    ci_TextView.get(i).setTextColor(Color.GREEN);
                }
            }
        }
    }

    private void initView() {
        img_practise = (ImageView) findViewById(R.id.practise);
        tv_question = (TextView) findViewById(R.id.choose_question_content);

        li_LinearLayout = new ArrayList<>();
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_a));
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_b));
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_c));
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_d));
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_e));
        li_LinearLayout.add((LinearLayout) findViewById(R.id.L_answer_f));
        li_size = li_LinearLayout.size();

        ci_TextView = new ArrayList<>();
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_a));
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_b));
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_c));
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_d));
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_e));
        ci_TextView.add((CircleTextView) findViewById(R.id.s_answer_f));

        tv_TextView = new ArrayList<>();
        tv_TextView.add((TextView) findViewById(R.id.s_answerA));
        tv_TextView.add((TextView) findViewById(R.id.s_answerB));
        tv_TextView.add((TextView) findViewById(R.id.s_answerC));
        tv_TextView.add((TextView) findViewById(R.id.s_answerD));
        tv_TextView.add((TextView) findViewById(R.id.s_answerE));
        tv_TextView.add((TextView) findViewById(R.id.s_answerF));
    }

}
