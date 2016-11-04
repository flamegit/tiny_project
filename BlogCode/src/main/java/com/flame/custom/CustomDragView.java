package com.flame.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CustomDragView extends View {

    private int point;
    private Paint paint;
    public CustomDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    @TargetApi(21)
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        int  w=canvas.getWidth();
        Path path =new Path();
       // path.cubicTo();

        int h=canvas.getHeight();
        Log.d("fxlts",w+":"+getWidth() );
       // canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);
        canvas.drawOval(w/2-30,h/2-30,w/2+30,h/2+30,paint);
        //canvas.setViewport();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scrollBy(20,20);
        return super.onTouchEvent(event);
    }
}
