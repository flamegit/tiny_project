package com.flame.custom;

/**
 * Created by Administrator on 2016/11/15.
 */



import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.test.R;

/**
 *
 * @author zhy
 *
 */
public class ColorAnimationView extends View {

    private int mStartX;
    private int mEndX;
    private int mBeginX;
    private Bitmap mBitmap ;
    private Paint mPaint;
    private float mProgress;

    public ColorAnimationView(Context context) {
        super(context, null);
    }
    public ColorAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setFilterBitmap(false);
        mPaint.setColor(0xfff54353);
        mProgress=0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Drawable drawable=getResources().getDrawable(R.mipmap.for_you);
        int width=w-getPaddingLeft()-getPaddingRight();
        int height=h-getPaddingTop()-getPaddingBottom();
        drawable.setBounds(0,0,width,height);
        mBeginX=width*3/4;
        mBitmap= Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(mBitmap);
        drawable.draw(c);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(mBitmap,getPaddingLeft(),getPaddingTop(),mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mStartX = (int) ((1-mProgress) * mBeginX );
        mEndX = (int) ((0.25*mProgress+0.75)*mBitmap.getWidth());
        canvas.drawRect(getPaddingLeft()+mStartX,getPaddingTop(),getPaddingLeft()+mEndX,getPaddingTop()+mBitmap.getHeight(),mPaint);
        mPaint.setXfermode(null);

    }

    public void playAnimation(){
        ObjectAnimator animator=ObjectAnimator.ofFloat(this,"progress",0f,1f).setDuration(1000);
        animator.start();
    }

    public void setProgress(float progress){
        mProgress=progress;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("fxlts","ontouch");
        playAnimation();
        return super.onTouchEvent(event);
    }

}
