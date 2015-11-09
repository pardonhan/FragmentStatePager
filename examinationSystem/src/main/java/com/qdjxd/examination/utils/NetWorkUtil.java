package com.qdjxd.examination.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 判断是否联网
 * @author asus
 *
 */
public class NetWorkUtil {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
