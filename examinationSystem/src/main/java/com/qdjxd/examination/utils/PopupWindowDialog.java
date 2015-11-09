package com.qdjxd.examination.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qdjxd.examination.R;

/**
 * 弹窗提示已经是最后一题，或者提示交卷
 * @author asus
 *
 */
public class PopupWindowDialog {
	private View.OnClickListener listener;
	private LayoutInflater mInflater;
	public PopupWindow popupWindow;
	public Button btn_ok,btn_cancel;
	public TextView tip_textview;
	public PopupWindowDialog(Context context,String title) {
		mInflater = LayoutInflater.from(context);
		View layout = mInflater.inflate(R.layout.popupwindow_dialog, null);
		LinearLayout title_re = (LinearLayout) layout.findViewById(R.id.top_linear);
		title_re.getBackground().setAlpha(180);
		tip_textview = (TextView) layout.findViewById(R.id.tip_textview);
		if(title!=null){
			tip_textview.setText(title);
		}
		btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
		btn_ok = (Button) layout.findViewById(R.id.btn_ok);
		popupWindow = new PopupWindow(layout,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, true);
		popupWindow.setFocusable(true);
		popupWindow.setAnimationStyle(R.style.AnimationFade);//set Animation
		btn_ok.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener);
	}
}
