package com.qdjxd.examination.baseInfo;

import com.qdjxd.examination.utils.SharedPreferencesHelper;

public class UserInfo {
	private static final String usernamekey = "USER_NAME_KEY";// User Name
	private static final String useridkey = "USER_ID_KEY";// User ID
	private static final String userorganiseidkey = "USER_ORGANISEID_KEY";// Organise ID
	private static final String userorganisenamekey = "USER_ORGANISENAME_KEY";// Organise
	private static final String useraliaskey = "USER_ALIAS_KEY";// Show Name
	private static final String userpwd = "USER_PWD";// User Password
	private static final String USERSEX = "USERSEX";
	
	// TODO add UserInfo and other get & set method
	public static void clear(SharedPreferencesHelper sp) {
		sp.putStringValue(usernamekey, null);
		sp.putStringValue(useridkey, null);
		sp.putStringValue(userorganiseidkey, null);
		sp.putStringValue(useraliaskey, null);
		sp.putStringValue(userorganisenamekey, null);
		// TODO  clear other UserInfo
	}

	// Set Password
	public static void setUserPwd(String pwd, SharedPreferencesHelper sp) {
		sp.putStringValue(userpwd, pwd);
	}

	// get Password
	public static String getUserPwd(SharedPreferencesHelper sp) {
		return sp.getStringValue(userpwd) == null ? "" : sp
				.getStringValue(userpwd);
	}

	/**
	 * TODO add UserInfo to Params
	 * 
	 * @param username
	 * @param userid
	 * @param useralias
	 * @param userorgid
	 * @param userorgname
	 * @param sp
	 */
	public static void setUserInfo(String username, String userid,
			String useralias, String userorgid, String userorgname,String sex,
			SharedPreferencesHelper sp) {
		setUserName(username, sp);
		setUserId(userid, sp);
		setUserAlias(useralias, sp);
		setUserOrgId(userorgid, sp);
		setUserOrgName(userorgname, sp);
		setUserSex(sex, sp);
	}

	// set User Name
	public static void setUserName(String username, SharedPreferencesHelper sp) {
		sp.putStringValue(usernamekey, username);
	}

	// set User ID
	public static void setUserId(String userid, SharedPreferencesHelper sp) {

		sp.putStringValue(useridkey, userid);
	}

	// Set User Name Show
	public static void setUserAlias(String useralias, SharedPreferencesHelper sp) {
		sp.putStringValue(useraliaskey, useralias);
	}

	// set Organise ID
	public static void setUserOrgId(String userorganiseid,
			SharedPreferencesHelper sp) {
		sp.putStringValue(userorganiseidkey, userorganiseid);
	}

	// set Organise Name
	public static void setUserOrgName(String userorgname,
			SharedPreferencesHelper sp) {
		sp.putStringValue(userorganisenamekey, userorgname);
	}
	
	public static void setUserSex(String sex,SharedPreferencesHelper sp){
		if(sex.equals("1")){
			sex = "男";
		}else{
			sex = "女";
		}
		sp.putStringValue(USERSEX, sex);
	}
	
	
	// get User Name 
	public static String getUserName(SharedPreferencesHelper sp) {
		return sp.getStringValue(usernamekey) == null ? "" : sp
				.getStringValue(usernamekey);
	}

	// get User ID
	public static String getUserId(SharedPreferencesHelper sp) {

		return sp.getStringValue(useridkey);
	}

	// get User ShowName
	public static String getUserAlias(SharedPreferencesHelper sp) {
		return sp.getStringValue(useraliaskey);
	}

	// get Organise Id
	public static String getUserOrgId(SharedPreferencesHelper sp) {
		return sp.getStringValue(userorganiseidkey);
	}

	// get Organise Name
	public static String getUserOrgName(SharedPreferencesHelper sp) {
		return sp.getStringValue(userorganisenamekey);
	}
	
	public static String getUserSex(SharedPreferencesHelper sp) {
		return sp.getStringValue(USERSEX);
	}
}
