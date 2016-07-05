package com.qdjxd.examination.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.qdjxd.examination.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Just on 16/6/18.
 */
public class CustomTitleView extends View {

    private String mTitleText;

    private int mTitleTextColor;

    private int mTitleTextSize;

    private Rect mBound;
    private Paint mPaint;

    public CustomTitleView(Context context) {
        super(context);
    }

    public CustomTitleView(Context context, AttributeSet attrs){
        super(context,attrs);
    }

    public CustomTitleView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        /*
         * get defStyle
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView,defStyle,0);
        int n = a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomTitleView_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case  R.styleable.CustomTitleView_titleTextColor:
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomTitleView_titleTextSize:
                    mTitleTextSize = (int) a.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleText = randomText();
                postInvalidate();
            }
        });

    }

    private String randomText(){
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size()<4){
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for(Integer i:set){
            sb.append(i);
        }
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText,getWidth()/2-mBound.width()/2,getHeight()/2-mBound.height()/2,mPaint);
    }
}
