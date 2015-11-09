package com.qdjxd.examination.utils;


import java.util.HashMap;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.content.Context;
import android.util.Log;

import com.qdjxd.examination.baseInfo.BaseInfo;


public class WebservicesUtil {
	
	
	/**
	 * ask webservice
	 
	 * @param METHOD_NAME
	 * @param params
	 * @return SoapObject
	 */
	public static Object CallDotNetWebService(Context context,String METHOD_NAME, List<Object> params) {
		DebugLog.v(BaseInfo.getWebURL(context));
		return CallDotNetWebService(context, METHOD_NAME, params,BaseInfo.getWebURL(context));
	}
	/**
	 * ask webservice
	 
	 * @param METHOD_NAME
	 * @param params
	 * @return SoapObject
	 */
	public static Object CallDotNetWebService(Context context,String METHOD_NAME, List<Object> params,String webUrl) {
		String SOAP_ACTION = BaseInfo.nameSpace+METHOD_NAME;
        String NAMESPACE = BaseInfo.nameSpace;
        String URL = webUrl;
        DebugLog.i(SOAP_ACTION+"---"+NAMESPACE+"---"+URL);
		try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            if (params != null && !params.isEmpty()) {
            	for(int i=0;i<params.size();i++){
            		Request.addProperty("arg"+i , params.get(i));
            	}
            }
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            MyAndroidHttpTransport transport= new MyAndroidHttpTransport(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            //处理返回值，返回值存储在json对象中。
            @SuppressWarnings("unused")
			SoapPrimitive resultString = (SoapPrimitive)soapEnvelope.getResponse();
            Object soapObject =  soapEnvelope.getResponse();
			if (soapObject != null && soapObject instanceof String) {
				soapObject = soapObject.toString().replace("null", "");
			}
			DebugLog.e(soapObject.toString());
			return soapObject;
		} catch (Exception ex) {
            Log.e("vik", "Error: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
	}

	/**
	 * 请求web service
	 */
	public static String CallWebService(Context context, String methodName,HashMap<String, Object> params) {
		return CallService(context, methodName, params);
	}

	private static String CallService(Context context, String methodName,HashMap<String, Object> params) {
		String result = null;
		String url = BaseInfo.getWebURL(context);
		String nameSpace = BaseInfo.nameSpace;
		String SOAP_ACTION = nameSpace + methodName;
		DebugLog.i(url+"---"+nameSpace+"---"+SOAP_ACTION);
		// 创建SoapObject实例
		SoapObject request = new SoapObject(nameSpace, methodName);
		// 生成调用web service方法的soap请求消息
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
		// 设置.net web service
		// envelope.dotNet=true;
		// 发送请求
		envelope.setOutputSoapObject(request);
		// 请求参数
		if (params != null && !params.isEmpty()) {
			int a = params.keySet().size();
			for (int i = 1; i <= a; i++) {
				request.addProperty("param" + i, params.get("param" + i));
			}
		}
		//
		AndroidHttpTransport androidHttpTrandsport = new AndroidHttpTransport(url);
		try {
			// web service请求
			androidHttpTrandsport.call(SOAP_ACTION, envelope);
			// 得到返回结果
			result = (String) envelope.getResponse().toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
