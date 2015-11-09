package com.qdjxd.examination.baseInfo;

import android.content.Context;

import com.qdjxd.examination.utils.SharedPreferencesHelper;

/**
 * 对项目基础数据的操作
 * 	密码保存
 * 	是否在线
 * @author asus
 *
 */
public class BaseInfo {
	static SharedPreferencesHelper sp;
	public static String nameSpace = "http://mobileService.mobile/";   //空间名,可修改   
	public static boolean cancel = false;
	public static String UserXml = "";
	public static String RunMode = "";
	public static String web_url = "";
	public static String password = "";
	public static String pdaid = "";
	public static String ipAddress="ipAddress";
	private static final String base_isLogin ="base_isLogin";
	private static final String base_isOnline = "base_isOnline";//是否在线
	private static final String base_isSavePwd = "base_isSavePwd";// 是否记住密码
	private static final String base_isSavaName = "base_isSaveName";//是否记住用户名
	private static final String defaultURL = "http://192.168.1.73:8080/ets/mobileService/MobileServiceImpl";//webservice地址
	private static final String URLKEY = "WebServicesUrlKey";
	
	public static String WebUrl(Context context) {
		if (web_url == null || "".equals(web_url)) {
			sp = new SharedPreferencesHelper(context, "examConfig");
			web_url = sp.getStringValue("Web_url");
		}
		return web_url;
	}

	
	public static String getWebURL(Context context) {
		SharedPreferencesHelper sp;
		sp = SharedPreferencesHelper.getDefault(context);
		String url = sp.getStringValue(URLKEY);
		if(url==null){
			return defaultURL;
		}
		return url;
	}
	/**保存ip*/
	public static void setIpAddress(Context context,String ipStr){
		SharedPreferencesHelper sp;
		sp = SharedPreferencesHelper.getDefault(context);
		sp.putStringValue(ipAddress, ipStr);
	}
	/**获取IP地址*/
	public static String getIpAddress(Context context){
		SharedPreferencesHelper sp;
		sp = SharedPreferencesHelper.getDefault(context);
		String ip = sp.getStringValue(ipAddress);
		if(ip==null){
			return "";
		}
		return ip;
	}
	public static void setWebURL(Context context, String url) {
		SharedPreferencesHelper sp;
		sp = SharedPreferencesHelper.getDefault(context);
		sp.putStringValue(URLKEY, url);
	}
	public static String getWebURLByIP(String ip) {
		return "http://" + ip + "/ets/mobileService/MobileServiceImpl";
		//return "http://192.168.1.73:8080/ets/mobileService/MobileServiceImpl";
	}
	//判断是否在线
	public static boolean IsOnline(SharedPreferencesHelper sp) {
		return sp.getBooleanValue(base_isOnline);
	}
	//设置是否在线
	public static void setOnline(SharedPreferencesHelper sp, boolean flag) {
		sp.putBooleanValue(base_isOnline, flag);
	}
	public static boolean IsSaveName(SharedPreferencesHelper sp){
		return sp.getBooleanValue(base_isSavaName);
	}
	public static void setSaveName(SharedPreferencesHelper sp,boolean flag){
		sp.putBooleanValue(base_isSavaName, flag);
	}
	//判断是否记住密码
	public static boolean IsSavePwd(SharedPreferencesHelper sp) {
		return sp.getBooleanValue(base_isSavePwd);
	}
	//设置记住密码
	public static void setSavePwd(SharedPreferencesHelper sp, boolean flag) {
		sp.putBooleanValue(base_isSavePwd, flag);
	}
	//判断是否登录
	public static boolean IsLogin(SharedPreferencesHelper sp){
		return sp.getBooleanValue(base_isLogin);
	}
	//设置登录状态
	public static void setLoginState(SharedPreferencesHelper sp,boolean flag){
		sp.putBooleanValue(base_isLogin, flag);
	}
}
