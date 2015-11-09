package com.qdjxd.examination.common;

import com.qdjxd.examination.common.webservices_impl.WebServicesCommonImpl;
import com.qdjxd.examination.utils.SharedPreferencesHelper;


public class CommonFactory {
	public static Common getCommon(SharedPreferencesHelper sp) {
			return new WebServicesCommonImpl();
	}
}
