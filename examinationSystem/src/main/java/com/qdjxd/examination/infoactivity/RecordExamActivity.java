package com.qdjxd.examination.infoactivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.qdjxd.examination.R;

public class RecordExamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_exam);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
