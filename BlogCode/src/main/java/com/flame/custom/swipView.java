package com.flame.custom;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/9/21.
 */
public class swipView extends FrameLayout {
    ViewDragHelper helper;
    public swipView(Context context, AttributeSet attrs) {
        super(context, attrs);

        ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                  getChildAt(0).offsetLeftAndRight(dx);
            }
            public int getViewHorizontalDragRange(View child) {
                return child.getWidth();
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }
        };
        helper=ViewDragHelper.create(this,callback);
   }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getChildAt(0).offsetLeftAndRight(getChildAt(0).getWidth());
    }

    //    @Override
//    public void setTranslationX(float translationX) {
//        int num=getChildCount();
//        if(num<2) {
//            super.setTranslationX(translationX);
//        }else {
//            getChildAt(1).setTranslationY(translationX);
//        }
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
         helper.processTouchEvent(event);
         return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev);
        //return super.onInterceptTouchEvent(ev);
    }
}
