package com.qdjxd.examination.utils;

import java.security.MessageDigest;

import com.qdjxd.examination.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MsgUtil {
	View.OnClickListener listener;
	/**
	 * 错误信息提示
	 * @param msg 提示信息
	 * @param context 显示页面
	 */
	public static void ShowErrMsg(String msg, Context context) {
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.msg_dialog, null);// 得到加载view
			TextView tipTextView = (TextView) v.findViewById(R.id.textView1);//提示文字
			tipTextView.setText(msg);// 设置加载信息
			final Dialog loadingDialog = new Dialog(context, R.style.my_dialog);// 创建自定义样式dialog
			Button btnCancle = (Button) v.findViewById(R.id.btnCancle);
			btnCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					loadingDialog.dismiss();
				}
			});
			loadingDialog.setCancelable(true);// 不可以用“返回键”取消
			loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
			loadingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*public static Dialog ShowTipDialog(Context context,String title,String msg){
		Dialog tipDialog = new Dialog(context,R.style.my_dialog);
		try{
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.tip_dialog, null);
			TextView titleView = (TextView) v.findViewById(R.id.textView1);
			TextView msgView =(TextView) v.findViewById(R.id.tip_message);
			Button ok_btn = (Button) v.findViewById(R.id.btnSure);
			titleView.setText(title);
			msgView.setText(msg);
			ok_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					
				}
			});
			tipDialog.setCancelable(false);
			tipDialog.addContentView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
		}catch(Exception e){
			e.printStackTrace();
		}
		return tipDialog;
	}*/
	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @param cancleable
	 * @return
	 */
	public static Dialog ShowLoadDialog(Context context, String title,
			String content, boolean cancleable) {
		Dialog loadingDialog = new Dialog(context, R.style.my_dialog);// 创建自定义样式dialog
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_image);
			TextView spaceshipText = (TextView) v.findViewById(R.id.loading_content);
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			spaceshipText.setText(content);
			loadingDialog.setCancelable(cancleable);// 不可以用“返回键”取消
			loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
			loadingDialog.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadingDialog;
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param content
	 * @return
	 */
	public static Dialog ShowLoadDialog(Context context, String title,String content) {
		Dialog loadingDialog = new Dialog(context, R.style.my_dialog);// 创建自定义样式dialog
		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_image);
			TextView spaceshipText = (TextView) v.findViewById(R.id.loading_content);
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			spaceshipText.setText(content);
			loadingDialog.setCancelable(true);// 不可以用“返回键”取消
			loadingDialog.setCanceledOnTouchOutside(false);
			loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
			loadingDialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadingDialog;
	}
	
	public static void ShowToast(String msg, Context context) {
		try {
			Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			LinearLayout mLayout = new LinearLayout(context);
			mLayout.setOrientation(LinearLayout.HORIZONTAL);
			//mLayout.setBackgroundResource(R.color.bottom_bar_DeepSkyBlue);
			TextView tv = new TextView(context);
			tv.setBackgroundResource(R.drawable.textview_toast_style);
			tv.setTextColor(0xffffffff);
			tv.setPadding(5, 5, 5, 5);
			tv.setText(msg);
			tv.setTextSize(18);
			mLayout.addView(tv);
			toast.setView(mLayout);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String ToMD5(String pstr) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			MessageDigest md5Temp = MessageDigest.getInstance("MD5");
			md5Temp.update(pstr.getBytes("UTF8"));
			byte[] md = md5Temp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
