package com.tencent.mobileqq.gasstation.utila;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 加载h5页面中防止软件盘遮挡屏幕
 */
public class AtKeyBoardUp {
    private static final String TAG = "AtKeyBoardUp";

    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.
    //使用这个类的简单调用assistactivity()一个活动已经有内容的视图设置(添加位置：紧接设置布局之后)。
    public static void assistActivity (Activity activity) {
        new AtKeyBoardUp(activity);
    }

    private Activity activity;
    private FrameLayout.LayoutParams frameLayoutParams;
    private View mChildOfContent;
    private int usableHeightPrevious;
    private int statusBarHeight;//状态栏高度

    private AtKeyBoardUp(Activity activity) {
        this.activity = activity;
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {//全局视图
                possiblyResizeChildOfContent();
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    /**可能调整内容的子内容*/
    private void possiblyResizeChildOfContent() {
        //获得有效高度
        int usableHeightNow = computeUsableHeight();
//        LogUtils.v(TAG,"有效高度："+usableHeightNow);
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
//            LogUtils.v(TAG,"全屏高度："+usableHeightSansKeyboard);

            //这个判断是为了解决19之前的版本不支持沉浸式状态栏导致布局显示不完全的问题(使用沉浸式状态栏时使用改判断)
//            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
//                Rect frame = new Rect();
//                activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//                int statusBarHeight = frame.top;
//                usableHeightSansKeyboard -= statusBarHeight;
//            }

            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
//            LogUtils.v(TAG, "默认高度：" + heightDifference);
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible（键盘可能是可见的）
                frameLayoutParams.height = usableHeightNow;
            } else {
                // keyboard probably just became hidden（键盘可能是隐藏的）
                frameLayoutParams.height = usableHeightSansKeyboard-statusBarHeight;
            }
            mChildOfContent.requestLayout();//重新设置自己位置
            usableHeightPrevious = usableHeightNow;
        }
    }

    /**计算有效高度*/
    private int computeUsableHeight() {

        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        statusBarHeight = r.top;

//        LogUtils.v(TAG,"状态栏高度："+statusBarHeight);
//        LogUtils.v(TAG,"我的布局高度（包含状态栏）："+r.bottom);//从顶到加载布局控件底部距离

       /* 这个判断是为了解决19之后的版本在弹出软键盘时，
          键盘和推上去的布局（adjustResize）之间有黑色区域的问题
          使用沉浸式状态栏时使用改判断（未检验用时调节）*/
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//            return (r.bottom - r.top)+statusBarHeight;
//        }

        return (r.bottom - r.top);
    }

}

