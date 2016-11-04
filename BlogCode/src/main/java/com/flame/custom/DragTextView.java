package com.flame.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/9/2.
 */
//TODO
public class DragTextView extends TextView {
    public DragTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }
}
