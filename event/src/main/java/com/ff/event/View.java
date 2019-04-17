package com.ff.event;

/**
 * description:
 * author: FF
 * time: 2019/4/14 21:45
 */
public class View {

    private static final String TAG = "View";

    private int left;
    private int top;
    private int right;
    private int bottom;

    private OnTouchListener mOnTouchListener;
    private OnClickListener mOnClickListener;

    public void setOnTouchListener(OnTouchListener l) {
        this.mOnTouchListener = l;
    }

    public void setOnClickListener(OnClickListener l) {
        this.mOnClickListener = l;
    }

    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * 判断View是否可以响应事件
     *
     * @param x 触摸点X轴坐标
     * @param y 触摸点Y轴坐标
     * @return true 可以响应事件
     */
    public boolean isContainer(int x, int y) {
        return x >= left && x < right && y >= top && y < bottom;// 事件坐标在控件内
    }

    /**
     * 分发事件
     *
     * @param ev {@link MotionEvent}事件
     * @return true 代表消费，例如setOnTouchListener或者setOnClickListener
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        System.out.println(TAG + "_" + getName() + " dispatchTouchEvent");

        boolean result = false;

        // 设置了监听setOnTouchListener，并且在onTouch中消费（返回true）
        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, ev)) {
            result = true;
        }
        // 如果上面onTouch消费了事件（返回true），onTouchEvent将不会执行，onClick也不会被调用
        if (!result && onTouchEvent(ev)) {
            result = true;
        }

        return result;
    }

    /**
     * onTouch事件
     *
     * @param event {@link MotionEvent}事件
     * @return true 代表消费
     */
    public boolean onTouchEvent(MotionEvent event) {
        if (/*event.getActionMasked() == MotionEvent.ACTION_UP
                && */mOnClickListener != null) {// 模拟效果，这里就不判断UP事件了
            mOnClickListener.onClick(this);
            return true;// 消费事件
        }
        return false;// 不消费事件
    }

    private String name;// 为了方便打印，增加字段

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public interface OnTouchListener {
        boolean onTouch(View v, MotionEvent event);
    }

    public interface OnClickListener {
        void onClick(View v);
    }
}
