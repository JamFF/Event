package com.ff.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * description:
 * author: FF
 * time: 2019/4/14 20:43
 */
public class MyView extends View {

    private static final String TAG = "MyView";

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, "dispatchTouchEvent start: action = " + event.getAction());
        boolean result = super.dispatchTouchEvent(event);
        Log.d(TAG, "dispatchTouchEvent end: action = " + event.getAction() + ", return = " + result);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent start: action = " + event.getAction());
        boolean result = super.onTouchEvent(event);// false，代表不消费，那么UP事件也不会再传递给它，onClick也不会执行
        Log.d(TAG, "onTouchEvent end: action = " + event.getAction() + ", return = " + result);
        return result;
    }
}
