package com.qdjxd.examination.examacitvity.listener;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.qdjxd.examination.adapter.DownloadListViewAdapter.ViewHolder;
import com.qdjxd.examination.baseInfo.BaseInfo;
import com.qdjxd.examination.baseInfo.UserInfo;
import com.qdjxd.examination.examacitvity.database.DataBaseUtils;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.HttpDownLoadUtils;
import com.qdjxd.examination.utils.MsgUtil;
import com.qdjxd.examination.utils.SharedPreferencesHelper;
import com.qdjxd.examination.utils.WebservicesUtil;

/**
 * 数据下载监听事件
 * @author asus
 *
 */
public class DownloadListener implements OnClickListener {
	SharedPreferencesHelper sp;
	@SuppressWarnings("unused")
	private ViewHolder mholder;
	private Context mContext;
	@SuppressWarnings("unused")
	private int position;
	private Dialog m_Dialog;
	private String classid;
	public DownloadListener(int postion,Context context,ViewHolder holder,String classid,SharedPreferencesHelper sp){
		this.sp = sp;
		this.mholder = holder;
		this.mContext = context;
		this.classid = classid;
		this.position = postion;
	}
	@Override
	public void onClick(View arg0) {
		if(m_Dialog == null){
			m_Dialog = MsgUtil.ShowLoadDialog(mContext, "请等待...", "正在下载，请稍候...",true);
		}
		else{
			m_Dialog.show();
		}
		new Thread() {
			@Override
			public void run() {
				// TODO download
				List<Object> params = new ArrayList<Object>();
				params.add(UserInfo.getUserId(sp)==null ? "" : UserInfo.getUserId(sp));
				params.add(classid);
				Object soapObject =  WebservicesUtil.CallDotNetWebService(mContext,"DownloadExam", params);//得到一个文件储存地址
				String result = soapObject.toString();
				if(result.equals("true")){
					StringBuffer url_stringBuffer = new StringBuffer("http://");
					url_stringBuffer.append(BaseInfo.getIpAddress(mContext));
					url_stringBuffer.append("/ets/resource/examination.txt");
					DebugLog.v(url_stringBuffer.toString());
					HttpDownLoadUtils httpdu = new HttpDownLoadUtils(mContext, url_stringBuffer.toString());
					httpdu.getDataSource();
					DataBaseUtils.insertIntoNewInfo(mContext);
					handler.sendEmptyMessage(1);
				}else{
					//提示下载失败
					handler.sendEmptyMessage(2);
				}
			}
		}.start();
	}

	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				if(m_Dialog!=null){
					m_Dialog.dismiss();
				}
				MsgUtil.ShowErrMsg("文件下载成功！", mContext);
			}
			if(msg.what==2){
				
				if(m_Dialog!=null){
					m_Dialog.dismiss();
				}
				MsgUtil.ShowErrMsg("文件下载失败，请重新下载！", mContext);
			}
		}
	}; 
}
