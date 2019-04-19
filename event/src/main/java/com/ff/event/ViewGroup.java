package com.ff.event;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 模仿 android 中的 ViewGroup
 * author: FF
 * time: 2019/4/14 21:46
 */
public class ViewGroup extends View {

    private static final String TAG = "ViewGroup";

    private List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];

    // First touch target in the linked list of touch targets.
    private TouchTarget mFirstTouchTarget;

    private boolean isIntercept;// 是否拦截

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);// 使用集合比较慢，使用数组根据下标查找速度更快，源码里面也使用了数组
        // 在JDK6前，设置长度效率高，之后的版本设置0
        // mChildren = childList.toArray(new View[childList.size()]);
        mChildren = childList.toArray(new View[0]);
    }

    /**
     * 分发事件
     *
     * @param ev {@link MotionEvent}事件
     * @return true 代表消费
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        System.out.println(TAG + "_" + getName() + " dispatchTouchEvent");
        boolean intercepted = onInterceptTouchEvent(ev);
        int actionMasked = ev.getActionMasked();
        TouchTarget newTouchTarget = null;
        boolean handled = false;

        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercepted) {
            // 没有取消，也没有被拦截
            if (actionMasked == MotionEvent.ACTION_DOWN) {// 处理down事件
                final View[] children = mChildren;
                // 遍历耗时，所以下面使用TouchTarget链表
                for (int i = children.length - 1; i >= 0; i--) {
                    // 反向循环的原因是dispatchTouchEvent是从上层往下层传递，最后添加的最先遍历，Z轴方向
                    View child = mChildren[i];
                    if (!child.isContainer(ev.getX(), ev.getY())) {
                        continue;
                    }

                    // 能够接受事件的child，分发给它
                    if (dispatchTransformedTouchEvent(ev, child)) {
                        newTouchTarget = addTouchTarget(child);
                        handled = true;
                        break;
                    }
                }
            }
        }

        if (mFirstTouchTarget == null) {
            // 拦截或者没有消费当前事件的child，传入null，调用父类View的dispatchTouchEvent
            handled = dispatchTransformedTouchEvent(ev, null);
        } else {
            // 有child消费事件，不会再调用自身的父类View的dispatchTouchEvent，也就不会回调自身的onTouch
            System.out.println(TAG + "_" + mFirstTouchTarget.child.getName() + " not dispatch onTouch");
        }
        return handled;
    }

    /**
     * 添加到链表中
     *
     * @param child 可以消费事件的View或者ViewGroup
     * @return {@link TouchTarget}
     */
    private TouchTarget addTouchTarget(View child) {
        final TouchTarget target = TouchTarget.obtain(child);// 从缓存中获取TouchTarget
        target.next = mFirstTouchTarget;// 完成链表结构
        mFirstTouchTarget = target;// 保存链表头
        return target;
    }

    /**
     * 分发处理
     * <p>
     * child为null，代表没有消费该事件的，将事件发送到此ViewGroup
     *
     * @param event {@link MotionEvent}事件
     * @param child View或者ViewGroup
     * @return true 代表消费
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        final boolean handled;
        if (child == null) {// 调用父类View的dispatchTouchEvent
            handled = super.dispatchTouchEvent(event);
        } else {// 调用子控件（可能是View也可能是ViewGroup）的dispatchTouchEvent
            handled = child.dispatchTouchEvent(event);
        }
        return handled;
    }

    /**
     * 拦截事件
     * <p>
     * ViewGroup默认不拦截，需要拦截重写该方法
     *
     * @param ev {@link MotionEvent}事件
     * @return true 代表拦截
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isIntercept;
    }

    /**
     * description: 为了更加高效的处理MOVE和UP事件，使用链表结构
     * 如果一个View处理了Down事件，保存在链表中，后面的MOVE和UP也会交给他处理，这时避免了再次遍历的耗时
     * author: FF
     * time: 2019/4/15 14:53
     */
    private static final class TouchTarget {
        private static final int MAX_RECYCLED = 32;
        // 当前缓存的View，The touched child view.
        public View child;
        // 回收池，设计在当前类中，使用静态保证一份（也是链表）
        private static TouchTarget sRecycleBin;
        // 针对并发使用synchronized，设计这个锁为0个长度的数组减小堆内存占用，源码中也是这样设计的
        private static final Object sRecycleLock = new Object[0];
        // 链表下一个元素
        public TouchTarget next;
        // 回收池大小
        private static int sRecycledCount;

        /**
         * 获取TouchTarget，从回收池中取，没有回收池才去new
         *
         * @param child 可以消费事件的View或者ViewGroup
         * @return {@link TouchTarget}
         */
        public static TouchTarget obtain(View child) {
            if (child == null) {
                throw new IllegalArgumentException("child must be non-null");
            }

            final TouchTarget target;
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {// 没有回收池，创建TouchTarget
                    target = new TouchTarget();
                } else {// 存在回收池，直接取TouchTarget
                    target = sRecycleBin;// 从回收池取首个TouchTarget
                    sRecycleBin = target.next;// 更新回收池首元素
                    sRecycledCount--;// 更新链表长度
                    target.next = null;// 将链表断掉，作为纯粹的TouchTarget返回
                }
            }
            target.child = child;// 关键一步，将view保存在TouchTarget中
            return target;
        }

        /**
         * 回收TouchTarget，加入到回收池
         */
        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("already recycled once");
            }

            synchronized (sRecycleLock) {
                if (sRecycledCount < MAX_RECYCLED) {
                    // 添加到链表（加入到回收池）
                    next = sRecycleBin;// 链表后移一个元素
                    sRecycleBin = this;// 调用该方法的TouchTarget作为链表首个元素
                    sRecycledCount += 1;// 更新链表长度
                } else {
                    // Android限制大小为32，所以layout中嵌套层级不能超过32
                    next = null;
                }
                child = null;
            }
        }
    }
}
