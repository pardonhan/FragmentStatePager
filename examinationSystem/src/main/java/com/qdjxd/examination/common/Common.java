package com.qdjxd.examination.common;

import com.qdjxd.examination.utils.SharedPreferencesHelper;

import android.content.Context;


public interface Common {
    boolean login(Context context, String username, String pwd,
                  SharedPreferencesHelper sp);
}
