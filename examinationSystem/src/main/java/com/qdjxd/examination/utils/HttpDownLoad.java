package com.qdjxd.examination.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.qdjxd.examination.baseInfo.BaseInfo;
/**
 * 文件下载
 * @author asus
 *
 */
 
public class HttpDownLoad {
	
	private String path; // 用于存文件的路径
	private String fileName;//文件名
	private String fileDir;//硬盘路径
	private String urlStr;

	private int fileSize;
	private int downLoadFileSize;

	private Boolean isCheckFileExist = false;// 是否检查文件重复
	private Handler handler;
	private ArrayList<HashMap<String,String>> list;
	
	private Context context;

	public HttpDownLoad(String path, String fileName, String fileDir,
			String urlStr, Boolean isCheckFileExist, Handler handler) 
	{
		this.path = path;
		this.fileName = fileName;
		this.fileDir = fileDir;
		this.urlStr = urlStr;
		this.isCheckFileExist = isCheckFileExist;
		this.handler = handler;
	}

	public HttpDownLoad(ArrayList<HashMap<String,String>> list,Boolean isCheckFileExist,
			Handler handler,Context context)
	{
		this.context = context;
		this.list = list;
		this.isCheckFileExist = isCheckFileExist;
		this.handler = handler;
	}
	
	/**
	 * 多文件下载
	 */
	public void downMultiFile() {
		try {
			Runnable r = new Runnable() {
				public void run() {
					try {
						getMultiDataSource();
					} catch (Exception e) {
						sendMsg(-4);
						e.printStackTrace();
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void downFile() {
		try {
			Runnable r = new Runnable() {
				public void run() {
					try {
						getDataSource();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(r).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取文件，并保存起来
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return -2文件不存在-4:下载出错 -3: 文件已经存在
	 */
	@SuppressWarnings("unused")
	protected void getMultiDataSource() {
		InputStream inputStream = null;
		for (int i = 0; i < list.size(); i++) {
			try {
				Map<String, String> map = list.get(i);
				String fileUrl = map.get("fileUrl");
				String filePath = map.get("filePath");
				String size = map.get("fileSize");
				if (isCheckFileExist && isFileExist(filePath)) {
					sendMsg(3, i);
					continue;
				} else {
					fileUrl = BaseInfo.WebUrl(context) + fileUrl;//文件地址
					URL url = new URL(fileUrl);
					HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
					inputStream = urlCon.getInputStream();
					this.fileSize = urlCon.getContentLength();/// 根据响应获取文件大小
					if (this.fileSize <= 0 && size!=null)
					{
						try {
							fileSize = Integer.parseInt(size);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// throw new RuntimeException("无法获知文件大小 ");
					if (inputStream == null){
						 throw new RuntimeException("stream is null");
					}
					File file = null;
					OutputStream output = null;
					try {
						String[] fileInfo = filePath.split("/");
						String fileDirTemp = "";
						for (int fileInfoIndex = 1; fileInfoIndex < fileInfo.length - 1; fileInfoIndex++) {
							fileDirTemp += fileInfo[fileInfoIndex] + "/";
						}
						this.path = fileInfo[0];
						this.fileDir = fileDirTemp;
						this.fileName = fileInfo[fileInfo.length - 1];

						// 创建SD卡上的目录
						File tempf = createSDDir();
						file = createSDFile(filePath);
						output = new FileOutputStream(file);
						byte buffer[] = new byte[1024];
						this.downLoadFileSize = 0;
						sendMsg(0);
						do {
							int numread = inputStream.read(buffer);
							if (numread == -1) {
								break;
							}
							output.write(buffer, 0, numread);
							this.downLoadFileSize += numread;
							sendMsg(1);
						} while (true);
						sendMsg(3, i);
						output.flush();
					} finally {
						try {
							if (output != null) {
								output.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
							sendMsg(-4);
							break;
						}
					}
					if (file == null) {
						sendMsg(-4);
					}
				}
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
				sendMsg(-2);
			} catch (IOException e) {
				e.printStackTrace();
				sendMsg(-4);
			} finally {
				try {
					if (inputStream != null) {
						inputStream.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
					sendMsg(-4);
				}
			}
		}
		sendMsg(4);
	}
	
	/**
	 * 读取文件，并保存起来
	 * 
	 * @param urlStr
	 * @param path
	 * @param fileName
	 * @return -2文件不存在-4:下载出错 -3: 文件已经存在
	 */
	protected void getDataSource() {
		InputStream inputStream = null;
		try {
			if (isCheckFileExist && isFileExist()) {
				sendMsg(-3);
			} else {
				URL url = new URL(urlStr);
				HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
				inputStream = urlCon.getInputStream();
				this.fileSize = urlCon.getContentLength();// 根据响应获取文件大小
				if (this.fileSize <= 0)
				if (inputStream == null)
					throw new RuntimeException("stream is null");

				File file = null;
				OutputStream output = null;
				try {
					// 创建SD卡上的目录
					@SuppressWarnings("unused")
					File tempf = createSDDir();
					file = createSDFile(path + fileDir + fileName);
					output = new FileOutputStream(file);
					byte buffer[] = new byte[1024];
					this.downLoadFileSize = 0;
					sendMsg(0);
					do {
						int numread = inputStream.read(buffer);
						if (numread == -1) {
							break;
						}
						output.write(buffer, 0, numread);
						this.downLoadFileSize += numread;
						sendMsg(1);
					} while (true);

					sendMsg(2);

					output.flush();
				} finally {
					try {
						if (output != null) {
							output.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
						sendMsg(-4);
					}
				}
				if (file == null) {
					sendMsg(-4);
				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			sendMsg(-2);
		} catch (IOException e) {
			e.printStackTrace();
			sendMsg(-4);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				sendMsg(-4);
			}
		}
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File createSDFile(String lj) throws IOException {
		File file = new File(lj);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 */
	public File createSDDir() {
		String[] dirs = fileDir.split("/");
		File file=new File(path + fileDir);
		String fileDir2="";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			for(int i=0;i<dirs.length;i++)
			{			 
				fileDir2+=dirs[i]+"/";				 
				File dir = new File(path + fileDir2);	
				if(!dir.exists())
				{
					dir.getAbsolutePath();
					dir.mkdir();
				}
			}
		}
		return file;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist() {
		File file = new File(path + fileDir + fileName);
		return file.exists();
	}

	public boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	private void sendMsg(int flag) {
		Message msg = new Message();
		msg.what = flag;
		if (flag == 0) {
			Bundle b = new Bundle();
			b.putInt("fileSize", this.fileSize);
			msg.setData(b);
		} else if (flag == 1) {
			Bundle b = new Bundle();
			b.putInt("fileSize", this.fileSize);
			b.putInt("downLoadFileSize", this.downLoadFileSize);
			msg.setData(b);
		}
		handler.sendMessage(msg);
	}
	private void sendMsg(int flag,int index) {
		Message msg = new Message();
		msg.what = flag;
		Bundle b = new Bundle();
		b.putInt("index", index);
		msg.setData(b);
		handler.sendMessage(msg);
	}
}
