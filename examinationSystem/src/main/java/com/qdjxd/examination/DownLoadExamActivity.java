/**
 * 文件名：DownLoadExamActivity
 * 版权：
 * 描述：用于下载试题
 * 创建人： hanfl
 * 创建时间：2015-08-27
 * 修改人:hanfl
 * 修改时间：2015-08-28
 * 修改单号：
 * 修改内容：
 */

package com.qdjxd.examination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qdjxd.examination.adapter.DownloadListViewAdapter;
import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.utils.MsgUtil;
import com.qdjxd.examination.utils.WebservicesUtil;

public class DownLoadExamActivity extends BaseActivity {
    private DownloadListViewAdapter dllvAdapter;
    private ArrayList<HashMap<String, String>> mList = new ArrayList<>();
    private Dialog m_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_exam);
        this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        ListView listview_download = (ListView) findViewById(R.id.downList);
        dllvAdapter = new DownloadListViewAdapter(DownLoadExamActivity.this, mList, sp);
        listview_download.setAdapter(dllvAdapter);
        if (BaseInfo.IsLogin(sp)) {
            getExamClassid();
        } else {
            MsgUtil.ShowErrMsg("请先登录！", DownLoadExamActivity.this);
        }
    }

    private void getExamClassid() {
        if (m_Dialog == null) {
            m_Dialog = MsgUtil.ShowLoadDialog(this, "请等待...", "正在获取数据，请稍候...", true);
        } else {
            m_Dialog.show();
        }
        new Thread() {
            @Override
            public void run() {
                List<Object> params = new ArrayList<>();
                Object soapObject = WebservicesUtil.CallDotNetWebService(DownLoadExamActivity.this, "GetExamClassid", params);
                try {
                    ArrayList<HashMap<String, String>> aList;//
                    Gson gs = new Gson();
                    aList = gs.fromJson(soapObject.toString(), new TypeToken<ArrayList<HashMap<String, String>>>() {
                    }.getType());
                    handler.sendMessage(handler.obtainMessage(1, aList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }

    Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (m_Dialog != null) {
                    m_Dialog.dismiss();
                }
                mList.clear();
                mList.addAll((ArrayList<HashMap<String, String>>) msg.obj);
                dllvAdapter.notifyDataSetChanged();
            }
        }
    };
}
