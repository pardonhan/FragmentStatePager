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
    public boolean login(Context context, String usercode, String pwd,SharedPreferencesHelper sp) {
        List<Object> params = new ArrayList<>();
        params.add(usercode);
        params.add(pwd);
        Object soapObject = WebservicesUtil.CallDotNetWebService(context, "UserLogin", params);
        if (!soapObject.equals("0")) {
            try {
                Gson gs = new Gson();
                ArrayList<HashMap<String, String>> aList;
                HashMap<String, String> map;
                aList = gs.fromJson(soapObject.toString(), new TypeToken<ArrayList<HashMap<String, String>>>() {
                }.getType());
                map = aList.get(0);
                UserInfo.setUserInfo(map.get("USERCODE"), map.get("USERID"), map.get("USERNAME"),
                        map.get("DEPTID"), map.get("ORGANISENAME"), map.get("SEX"), sp);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }
}
