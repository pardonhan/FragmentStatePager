package com.qdjxd.examination.baseInfo;

import com.qdjxd.examination.utils.SharedPreferencesHelper;
/**
 * 记录当前答题的序号
 * @author asus
 *
 */
public class CurrentInfo {
	
	static SharedPreferencesHelper sp;
	
	public static final String RANDOM_EXAM ="RANDOM_EXAM";//随机答题
	public static final String ORDINAL_EXAM ="ORDINAL_EXAM";//顺序答题
	public static final String SINGLE_CHOOSE = "SINGLE_CHOOSE";//单选题
	public static final String MULTY_CHOOSE="MULTY_CHOOSE";//多选题
	public static final String TRUEORFALSE="TRUEORFALSE";//判断题
	public static final String AUTOEXAM = "AUTOEXAM";
	
	
	public void setRandomExam(SharedPreferencesHelper sp,int current){
		sp.putIntValue(RANDOM_EXAM, current);
	}
	
	public Integer getRandomExam(SharedPreferencesHelper sp){
		return sp.getIntValue(RANDOM_EXAM);
	}
	public void setOrdinalExam(SharedPreferencesHelper sp,int current){
		sp.putIntValue(ORDINAL_EXAM, current);
	}
	
	public Integer getOrdinalExam(SharedPreferencesHelper sp){
		return sp.getIntValue(ORDINAL_EXAM);
	}
	public void setSingleChoose(SharedPreferencesHelper sp,int current){
		sp.putIntValue(SINGLE_CHOOSE, current);
	}
	public Integer getSingelChoose(SharedPreferencesHelper sp){
		return sp.getIntValue(SINGLE_CHOOSE);
	}
	public void setMultyChoose(SharedPreferencesHelper sp,int current){
		sp.putIntValue(MULTY_CHOOSE, current);
	}
	public Integer getMultyChoose(SharedPreferencesHelper sp){
		return sp.getIntValue(MULTY_CHOOSE);
	}
	public void setTrueOrFalse(SharedPreferencesHelper sp,int current){
		sp.putIntValue(TRUEORFALSE, current);
	}
	public Integer getTrueInteger(SharedPreferencesHelper sp){
		return sp.getIntValue(TRUEORFALSE);
	}

	public void setAutoExam(SharedPreferencesHelper sp,int current){
		sp.putIntValue(AUTOEXAM, current);
	}
	public Integer getAutoExam(SharedPreferencesHelper sp){
		return sp.getIntValue(AUTOEXAM);
	}
}
