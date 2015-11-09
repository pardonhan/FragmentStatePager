/*
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

For more information, please refer to <http://unlicense.org/>
 */

package com.qdjxd.examination.utils;


import android.util.Log;

import com.qdjxd.examination.BuildConfig;

/**
 * 
 *   Create a simple and more understandable Android logs.
 */

public class DebugLog {
	/* isDebug is used to control the log output */
	static final boolean isDebug = true;
	static String className;
	static String methodName;
	static int lineNumber;

	private DebugLog() {
		/* Protect from instantiations */
	}

	public static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}

	private static String createLog(String log) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);

		return buffer.toString();
	}

	private static void getMethodNames(StackTraceElement[] sElements) {
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void e(Object message) {
		if (isDebug) {
			if (!isDebuggable())
				return;
			// Throwable instance must be created before any methods
			getMethodNames(new Throwable().getStackTrace());
			Log.e(className, createLog(message.toString()));
		}
	}

	public static void i(Object message) {
		if (isDebug) {
			if (!isDebuggable())
				return;

			getMethodNames(new Throwable().getStackTrace());
			Log.i(className, createLog(message.toString()));
		}
	}

	public static void d(Object message) {
		if (isDebug) {
			if (!isDebuggable())
				return;

			getMethodNames(new Throwable().getStackTrace());
			Log.d(className, createLog(message.toString()));
		}
	}

	public static void v(Object message) {
		if (isDebug) {

			if (!isDebuggable())
				return;

			getMethodNames(new Throwable().getStackTrace());
			Log.v(className, createLog(message.toString()));
		}
	}

	public static void w(Object message) {
		if (isDebug) {
			if (!isDebuggable())
				return;

			getMethodNames(new Throwable().getStackTrace());
			Log.w(className, createLog(message.toString()));
		}
	}

	public static void wtf(Object message) {
		if (isDebug) {
			if (!isDebuggable())
				return;

			getMethodNames(new Throwable().getStackTrace());
			Log.wtf(className, createLog(message.toString()));
		}
	}

}
