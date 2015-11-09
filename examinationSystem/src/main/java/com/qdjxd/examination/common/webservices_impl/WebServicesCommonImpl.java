package com.qdjxd.examination.common.webservices_impl;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qdjxd.examination.baseInfo.UserInfo;
import com.qdjxd.examination.common.Common;
import com.qdjxd.examination.utils.DebugLog;
import com.qdjxd.examination.utils.SharedPreferencesHelper;
import com.qdjxd.examination.utils.WebservicesUtil;

import android.content.Context;

public class WebServicesCommonImpl implements Common {
	@Override
	public boolean login(Context context, String usercode, String pwd,
			SharedPreferencesHelper sp) {
		List<Object> params = new ArrayList<Object>();
		params.add(usercode);
		params.add(pwd);
		DebugLog.i(usercode);
		DebugLog.i(pwd);
		Object soapObject =  WebservicesUtil.CallDotNetWebService(context,"UserLogin", params);
		if(!soapObject.equals("0")){
			try {
				Gson gs = new Gson();
				ArrayList<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				aList = gs.fromJson(soapObject.toString(),new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType());
				map = aList.get(0);
				DebugLog.i("USERCODE-->"+map.get("USERCODE").toString());
				DebugLog.i("USERID-->"+map.get("USERID").toString());
				DebugLog.i("USERNAME-->"+map.get("USERNAME").toString());
				DebugLog.i("DEPTID-->"+map.get("DEPTID").toString());
				DebugLog.i("ORGANISENAME-->"+map.get("ORGANISENAME").toString());
				UserInfo.setUserInfo(map.get("USERCODE").toString(), map.get("USERID").toString(), map.get("USERNAME").toString(), 
						map.get("DEPTID"), map.get("ORGANISENAME").toString(),map.get("SEX").toString(), sp);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}
}
