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
public class MyViewGroup02 extends RelativeLayout {

    private static final String TAG = "MyViewGroup02";

    public MyViewGroup02(Context context) {
        this(context, null);
    }

    public MyViewGroup02(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup02(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent: ");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        return super.onTouchEvent(event);
    }
}
