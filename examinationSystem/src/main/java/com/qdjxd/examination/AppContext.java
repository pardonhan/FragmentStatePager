package com.qdjxd.examination;

import android.app.Application;

import com.qdjxd.examination.utils.DataBaseHelper;

import java.io.File;

/**
 * Created by Just on 2016/7/15.
 * AppContext
 */
public class AppContext extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
    }

    /**
     * 判断数据库文件是否存在，否则创建
     */
    private void initDataBase() {
        String data_path1 = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/data/examinationSystem/ESDataBase/" + DataBaseHelper.DB_NAME_BASE;
        File dataBase = new File(data_path1);
        String data_path2 = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/data/examinationSystem/ESDataBase/" + DataBaseHelper.DB_NAME_LOCAL;
        File dataBase2 = new File(data_path2);
        if (!dataBase.exists()||!dataBase2.exists()) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    DataBaseHelper db = new DataBaseHelper(AppContext.this);
                    db.createDataBase();
                }
            }.start();
        }

    }
}
