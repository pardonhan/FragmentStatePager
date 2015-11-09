package com.qdjxd.examination.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class HttpDownLoadUtils {
	private String FILEPATH;//文件存储路径
	private String urlPath;
	@SuppressWarnings("unused")
	private Context context;
	private int fileSize;
	@SuppressWarnings("unused")
	private int downLoadFileSize;
	public HttpDownLoadUtils(Context context,String urlpath) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.urlPath = urlpath;
		FILEPATH = android.os.Environment  
				.getExternalStorageDirectory().getAbsolutePath() +
				"/data/examinationSystem/ESDataBase/";
		DebugLog.v(FILEPATH);
	}
	
	/**
	 * 读取文件，并保存起来
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return -2文件不存在-4:下载出错 -3: 文件已经存在
	 */
	public void getDataSource() {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			inputStream = urlCon.getInputStream();
			fileSize = urlCon.getContentLength();// 根据响应获取文件大小
			if (fileSize <= 0){
				if (inputStream == null){
					throw new RuntimeException("stream is null");
				}
			}
			File file = null;
			OutputStream output = null;
			try {
				file = createSDDir();// 创建SD卡上的目录
				
				output = new FileOutputStream(file);
				byte buffer[] = new byte[1024];
				downLoadFileSize = 0;
				do {
					int numread = inputStream.read(buffer);
					if (numread == -1) {
						break;
					}
					output.write(buffer, 0, numread);
					this.downLoadFileSize += numread;
				} while (true);
				output.flush();
			} finally {
				try {
					if (output != null) {
						output.close();
						DebugLog.v("download-->output.close();");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
					DebugLog.v("download-->inputStream.close();");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private File createSDDir() throws IOException{
		String[] urls = urlPath.split("/");
		DebugLog.v(urls[urls.length-1]);
		String fileName = urls[urls.length-1];
		File file = new File(FILEPATH);
		if(file.exists()){
			file.mkdir();
		}
		File filetxt = new File(FILEPATH+fileName);
		if(filetxt.exists()){
			filetxt.delete();
		}
		filetxt.createNewFile();
		return filetxt;
	}
}
