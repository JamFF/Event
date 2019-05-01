package com.ff.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * description:
 * author: FF
 * time: 2019/4/14 20:41
 */
public class MyViewGroup01 extends RelativeLayout {

    private static final String TAG = "MyViewGroup01";

    public MyViewGroup01(Context context) {
        this(context, null);
    }

    public MyViewGroup01(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent start: action = " + ev.getAction());
        boolean result = super.dispatchTouchEvent(ev);
        Log.d(TAG, "dispatchTouchEvent end: action = " + ev.getAction() + ", return = " + result);
        return result;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent start: action = " + ev.getAction());
        boolean result = super.onInterceptTouchEvent(ev);
        Log.d(TAG, "onInterceptTouchEvent end: action = " + ev.getAction() + ", return = " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent start: action = " + event.getAction());
        boolean result = super.onTouchEvent(event);
        Log.d(TAG, "onTouchEvent end: action = " + event.getAction() + ", return = " + result);
        return result;
    }
}
