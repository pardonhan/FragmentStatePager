package com.qdjxd.examination.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;

import com.qdjxd.examination.BaseActivity;
import com.qdjxd.examination.R;
import com.qdjxd.examination.baseInfo.BaseInfo;


public class DataBaseCommon {
	
	Context context;
	Dialog dialog;
	
	private static final String databaseVersionkey = "databaseVersionkey";
	private static final int downlaoddbcode = 102;
	private static final int downlaoddbsuccess = 103;
	private static final int downlaoddbfileerr = 104;
	
	int version = 2;
	int nextversion = 2;
	
	String DB_PATH = "/data/examinationSystem/ESDataBase/";
	private final String filename = "ANDROID.db";
	
	public void getDBVersion() {
		version = ((BaseActivity) context).sp.getIntValue(databaseVersionkey,version);
	}
	
	public void setDBVersion(int version){
		((BaseActivity)context).sp.putIntValue(databaseVersionkey, version);
	}
	
	public DataBaseCommon(Context context) {
		super();
		this.context = context;
		try {
			DB_PATH = android.os.Environment  
					.getExternalStorageDirectory().getAbsolutePath() +
					"/data/examinationSystem/ESDataBase/";
		} catch (Exception e) {
			DB_PATH  = "/data/examinationSystem/ESDataBase/";
		}
	}
	
	public void chenckVersion(){
		new Thread(){
			@Override
			public void run() {
				try {
					getDBVersion();
					if (nextversion > version) {
						handler.sendEmptyMessage(downlaoddbcode);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.run();
			}
			
		}.start();
	}
	
	private void downloadDB(){
		try {
			final MyDialog dialog = new MyDialog(context, R.style.my_dialog);
			dialog.setMytitle("基础数据有更新是否下载！");
			dialog.setOkBtnOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
					download();
				}
			});
			View view = new View(context);
			dialog.setContentView(view,new LayoutParams(0,0));
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void download() {
		if (dialog == null)
			dialog = MsgUtil.ShowLoadDialog(context, "", "", false);
		else
			dialog.show();

		del(DB_PATH + filename);//删除当前数据库文件

		HttpDownLoad down = new HttpDownLoad(DB_PATH, filename, "",
				BaseInfo.getIpAddress(context) + "/androidSqlLite", true, handler);
		down.downFile();
	}

	private void checkDataBase(final String filename) {
		new Thread() {
			@Override
			public void run() {
				try {
					boolean tempflag = false;
					DataBaseHelper db = new DataBaseHelper(context,filename);
					db.openDataBase();
					String sql = "select * from sqlite_master";
					Cursor cs = db.queryData(sql);
					if(cs!=null){
						if(cs.moveToNext()){
							tempflag = true;
						}
						cs.close();
					}
					db.close();
					
					if (tempflag) {
						del(DB_PATH+DataBaseHelper.DB_NAME_BASE);
						copyFile(new File(DB_PATH+filename), new File(DB_PATH+DataBaseHelper.DB_NAME_BASE));
					}
					handler.sendEmptyMessage(downlaoddbsuccess);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendEmptyMessage(downlaoddbfileerr);
				super.run();
			}
			
		}.start();
	}
	
	public void del(String filep) {
		try {
			File dbf = new File(filep);
			if (dbf.exists()) {
				dbf.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 复制文件
    public static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
        	// 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 	新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
         // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
        	 // 关闭流
            if (inBuff != null)
                inBuff.close();
            if (outBuff != null)
                outBuff.close();
        }
    }
	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				checkDataBase(filename);
				dialog.dismiss();
			} else if (msg.what < 0) {
				MsgUtil.ShowErrMsg("下载基础数据失败！", context);
				dialog.dismiss();
			} else if (msg.what == downlaoddbcode) {
				downloadDB();
			} else if (msg.what == downlaoddbsuccess) {
				dialog.dismiss();
				setDBVersion(nextversion);
				MsgUtil.ShowErrMsg("下载基础数据成功！", context);
			} else if (msg.what == downlaoddbfileerr) {
				MsgUtil.ShowErrMsg("下载基础数据文件损坏！", context);
				dialog.dismiss();
			}
		}
	};
}
