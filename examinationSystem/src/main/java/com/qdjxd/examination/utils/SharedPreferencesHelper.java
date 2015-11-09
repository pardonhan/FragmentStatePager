package com.qdjxd.examination.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * android 一种存储方式，存储到xml中
 *
 */
public class SharedPreferencesHelper {
	
	SharedPreferences sp;
	SharedPreferences.Editor editor;
	Context context;
	private static final String filename = "examConfig";
	
	public static SharedPreferencesHelper getDefault(Context c) {
		return new SharedPreferencesHelper(c, filename);
	}
	
	public SharedPreferencesHelper(Context c,String name) {
		context = c;
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public void putStringValue(String key, String value) {
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getStringValue(String key) {
		return sp.getString(key, null);
	}

	public void putBooleanValue(String key, Boolean value) {
		editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public Boolean getBooleanValue(String key) {
		return sp.getBoolean(key, false);
	}

	public void putFloatValue(String key, Float value) {
		editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();
	}

	public Float getFloatValue(String key) {
		return sp.getFloat(key, 0);
	}

	public void putIntValue(String key, int value) {
		editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getIntValue(String key) {
		return sp.getInt(key, 0);
	}
	
	public int getIntValue(String key,int defaultV) {
		return sp.getInt(key, defaultV);
	}

	public void putLongValue(String key, Long value) {
		editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public Long getLongValue(String key) {
		return sp.getLong(key, 0);
	}
}
