package com.qdjxd.examination.infoactivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.qdjxd.examination.R;

public class CollectExamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_exam);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
