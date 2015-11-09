package com.qdjxd.examination.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qdjxd.examination.R;


public class MyDialog extends Dialog {

	View.OnClickListener listener;
	String mytitle;
	int dialogIcon = R.drawable.dialog_icon;
	boolean hasBtn = true;
	public MyDialog(Context context) {
		super(context,R.style.my_dialog);
	}

	public MyDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	public boolean isHasBtn() {
		return hasBtn;
	}

	public void setHasBtn(boolean hasBtn) {
		this.hasBtn = hasBtn;
	}

	public String getMytitle() {
		return mytitle==null?"":mytitle;
	}

	public void setMytitle(String mytitle) {
		this.mytitle = mytitle;
	}

	public void setOkBtnOnClickListener(View.OnClickListener listener)
	{
		this.listener = listener;
	}
	
	public int getDialogIcon() {
		return dialogIcon;
	}

	public void setDialogIcon(int dialogIcon) {
		this.dialogIcon = dialogIcon;
	}
	
	
	
	@Override
	public void setContentView(View view ,LayoutParams params) {
		LayoutInflater inflater = LayoutInflater.from(this.getContext());
		View v = inflater.inflate(R.layout.my_dialog, null);// �õ�����view
		
		ImageView icon = (ImageView) v.findViewById(R.id.imageView1);
		icon.setImageResource(dialogIcon);
		LinearLayout content = (LinearLayout) v.findViewById(R.id.contentView);
		
		if (params != null) {
			content.setLayoutParams(params);
			content.addView(view);

		} else {
			content.addView(view,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}
		TextView title = (TextView) v.findViewById(R.id.textView1);
		title.setText(getMytitle());
		
		View btnlayout = v.findViewById(R.id.button);
		DensityUtil du = new DensityUtil(getContext());
		btnlayout.setLayoutParams(new LinearLayout.LayoutParams(du.getWidth()*3/4,LayoutParams.WRAP_CONTENT));
		if (!hasBtn) {
			v.findViewById(R.id.button).setVisibility(View.GONE);
		} else {
			Button btnOk = (Button) v.findViewById(R.id.btnOk);
			Button btnCancle = (Button) v.findViewById(R.id.btnCancle);
			btnOk.setOnClickListener(listener);
			btnCancle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dismiss();
				}
			});
		}
		super.setContentView(v);
	}
	
	
	@Override
	public void setContentView(View view) {
		setContentView(view, null);
	}

}
