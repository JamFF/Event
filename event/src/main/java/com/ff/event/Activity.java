package com.ff.event;

/**
 * description:
 * author: FF
 * time: 2019/4/14 22:42
 */
public class Activity {

    private static final String TAG = "Activity";

    public static void main(String[] args) {

        final ViewGroup viewGroup = new ViewGroup(0, 0, 1080, 1920);
        viewGroup.setName("顶级容器");

        final ViewGroup viewGroup1 = new ViewGroup(0, 0, 500, 500);
        viewGroup1.setName("二级容器");

        final View view = new View(0, 0, 200, 200);
        view.setName("三级View");

        viewGroup1.addView(view);
        viewGroup.addView(viewGroup1);

        viewGroup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(TAG + "_" + viewGroup + " onTouch");
                return false;
            }
        });
        viewGroup1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(TAG + "_" + viewGroup1 + " onTouch");
                return false;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(TAG + "_" + view + " onClick");
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println(TAG + "_" + view + " onTouch");
                return false;// 不消费，事件会继续传递
                // return true;// 消费，事件不会传递到父容器的onTouch，也不会调用自身的onClick
            }
        });

        // 点击事件
        MotionEvent motionEvent = new MotionEvent(100, 100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);

        // 顶级容器分发
        viewGroup.dispatchTouchEvent(motionEvent);
    }
}
