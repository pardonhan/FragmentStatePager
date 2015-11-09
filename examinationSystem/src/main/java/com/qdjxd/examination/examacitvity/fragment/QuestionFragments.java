package com.qdjxd.examination.examacitvity.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qdjxd.examination.examacitvity.bean.AnswerInfo;
import com.qdjxd.examination.examacitvity.bean.QuestionInfo;

import java.util.Arrays;
import java.util.List;

/**
 * Created by asus on 2015/11/09.
 */
public class QuestionFragments extends Fragment {
    public static final String TAG = QuestionFragment.class.getSimpleName();
    private View questionView;

    private QuestionInfo questionInfo;
    private List<AnswerInfo> answerItem;

    private final List<String> list = Arrays.asList("正确", "错误");
    public QuestionFragments(QuestionInfo qf){
        this.questionInfo = qf;
        this.answerItem = qf.answerItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
