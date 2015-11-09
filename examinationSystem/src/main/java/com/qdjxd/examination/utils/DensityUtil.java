package com.qdjxd.examination.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 当前屏幕的参数
 * @author 	HanFl
 * Density 
 */
@SuppressLint("NewApi") 
public class DensityUtil {
	
	// 当前屏幕的densityDpi 
	private static float dmDensityDpi = 0.0f; 
	private static DisplayMetrics dm; 
	private static float scale = 0.0f;
	private Context context;
	public int height;	
	public int width;
	/**  根据构造函数获得当前手机的屏幕系数 */ 
	public DensityUtil(Context context) { 
		this.context=context;
		// 获取当前屏幕 
		dm = new DisplayMetrics(); 
		dm = context.getApplicationContext().getResources().getDisplayMetrics(); 
		//设置DensityDpi 
		setDmDensityDpi(dm.densityDpi); 
		//密度因子 
		scale = getDmDensityDpi() / 160;
		this.width=dm.widthPixels;
		this.height=dm.heightPixels;
	} 
	
	/**  当前屏幕的density因子 */ 
	public static float getDmDensityDpi() { 
		return dmDensityDpi; 
	} 
	/** 
	* 当前屏幕的density因子 
	* @param DmDensity 
	*/ 
	public static void setDmDensityDpi(float dmDensityDpi) {
		DensityUtil.dmDensityDpi = dmDensityDpi;
	}
	
	/**  密度转换像素 */ 
	public int dip2px(float dipValue) {
		return (int) (dipValue * scale + 0.5f);
	} 
	/**  像素转换密度 */ 
	public int px2dip(float pxValue) {
		return (int) (pxValue / scale + 0.5f);
	}

	public boolean getOrientation() {
		Boolean orie = true;
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			orie = true;// 是竖屏
		} else {
			orie = false;
		}
		return orie;
	}

	@Override 
	public String toString() { 
		return " dmDensityDpi:" + dmDensityDpi; 
	}

	public int getHeight() {
		this.height=dm.heightPixels;
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		this.width=dm.widthPixels;
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
