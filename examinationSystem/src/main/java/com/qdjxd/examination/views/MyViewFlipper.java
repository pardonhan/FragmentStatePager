package com.qdjxd.examination.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import com.qdjxd.examination.R;
import com.qdjxd.examination.views.MyGestureListener.OnFlingListener;

public class MyViewFlipper extends ViewFlipper implements OnFlingListener {

	//手势监听类
    private GestureDetector mGestureDetector = null;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    private OnViewFlipperListener mOnViewFlipperListener = null;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    public MyViewFlipper(Context context) {
        super(context);
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    @SuppressWarnings("deprecation")
	public void setOnViewFlipperListener(OnViewFlipperListener mOnViewFlipperListener) {
        this.mOnViewFlipperListener = mOnViewFlipperListener;
        //初始化自定义滑动事件监听器
        MyGestureListener myGestureListener = new MyGestureListener();
        //绑定自定义的滑动监听器
        myGestureListener.setOnFlingListener(this);
        mGestureDetector = new GestureDetector(myGestureListener);
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (null != mGestureDetector) {
            return mGestureDetector.onTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    /**向下一条滑动事件**/
    @Override
    public void flingToNext() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getNextView(), 0);
            if (0 != childCnt) {
                setInAnimation(getContext(), R.anim.push_left_in);
                setOutAnimation(getContext(), R.anim.push_left_out);
                setDisplayedChild(0);
            }
        }
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    /**向上一条滑动事件**/
    @Override
    public void flingToPrevious() {
        if (null != mOnViewFlipperListener) {
            int childCnt = getChildCount();
            if (childCnt == 2) {
                removeViewAt(1);
            }
            addView(mOnViewFlipperListener.getPreviousView(), 0);
            if (0 != childCnt) {
                setInAnimation(getContext(), R.anim.push_right_in);
                setOutAnimation(getContext(), R.anim.push_right_out);
                setDisplayedChild(0);
            }
        }
    }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    /**自定义View变化监听回调接口**/
    public interface OnViewFlipperListener {
        View getNextView();   //获取下一页View
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
        View getPreviousView();  //获取上一页View
    }

}
