package com.tencent.mobileqq.gasstation.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Zhangchen on 2017/6/26.
 */

public class MyItemOrition extends RecyclerView.ItemDecoration {

    /**
     * 水平方向
     */
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    /**
     * 垂直方向
     */
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private int mHeight = 1;//分割线高度
    private int mColor;//分割线颜色
    private int orientation;//分割线方向

    private Paint mPaint;

    public MyItemOrition() {
        this(VERTICAL);
    }

    public MyItemOrition(int orientation ) {
        this.orientation = orientation;
        mPaint = new Paint();

    }

    //通过Rect为每个Item设置偏移，用于绘制Decoration
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0){
            outRect.top = mHeight;
        }
    }

    //通过该方法，在Canvas上绘制内容，在绘制Item之前调用。（如果没有通过getItemOffsets设置偏移的话，Item的内容会将其覆盖）
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = parent.getChildAt(i);
            if (orientation == HORIZONTAL) {
                final int top = parent.getTop();
                final int left = childView.getLeft() - mHeight;
                final int right = childView.getLeft();
                final int bottom = parent.getBottom();
                c.drawRect(left, top, right, bottom, mPaint);
            } else if (orientation == VERTICAL) {
                final int left = parent.getLeft();
                final int right = parent.getRight();
                final int bottom = childView.getTop();
                final int top = bottom - mHeight;
                c.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //通过该方法，在Canvas上绘制内容,在Item之后调用。(画的内容会覆盖在item的上层)
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 设置分隔线颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(color);
    }

    /**
     * 设置分隔线高度
     *
     * @param height
     */
    public void setHeight(int height) {
        this.mHeight = height;
    }

}
