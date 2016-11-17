package com.flame.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CustomDragView extends View {
    final static int IDLE=0;
    final static int MOVE_INSIDE=1;
    final static int MOVE_OUTSIDE=2;

    private Paint paint;
    private Paint textPaint;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private double sin;
    private double cos;
    private int originRadius;
    private int currRadius;
    private int maxLength;

    Path path;
    int status;

    private String text="99+";
    private int ascent;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }


    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            result = (int) textPaint.measureText(text) + getPaddingLeft()
                    + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        ascent = (int) textPaint.ascent();
        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = (int) (-ascent + textPaint.descent()) + getPaddingTop()
                    + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }



    public CustomDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        textPaint=new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(40);
        startX=40;
        startY=40;

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        currRadius=originRadius=30;
        maxLength=10*originRadius;

        path=new Path();
        status=IDLE;
        paint.setAntiAlias(true);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(status==MOVE_INSIDE){

            path.reset();
            int startUpX= (int) (startX+currRadius*sin);
            int startUpY= (int) (startY-currRadius*cos);
            int startDownX= (int) (startX-currRadius*sin);
            int startDownY= (int) (startY+currRadius*cos);

            int endUpX= (int) (endX+originRadius*sin);
            int endUpY= (int) (endY-originRadius*cos);
            int endDownX= (int) (endX-originRadius*sin);
            int endDownY= (int) (endY+originRadius*cos);
            int midX=(startX+endX)/2;
            int midY=(startY+endY)/2;

            path.moveTo(startUpX,startUpY);
            path.quadTo(midX,midY,endUpX,endUpY);
            path.lineTo(endDownX,endDownY);
            path.quadTo(midX,midY,startDownX,startDownY);
            path.close();

            canvas.drawCircle(startX,startY,currRadius,paint);
            canvas.drawPath(path,paint);

            canvas.save();
            canvas.translate(endX-startX,endY-startY);
            canvas.drawRoundRect(0,0,getWidth(),getHeight(),20,20,paint);
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent, textPaint);
            canvas.restore();
        }else {
            canvas.drawRoundRect(0,0,getWidth(),getHeight(),23,23,paint);
            Log.d("fxlts",getHeight()+"");
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent, textPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //startX=w/2;
        //startY=h/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                status=MOVE_INSIDE;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int x=(int) event.getX();
                int y=(int) event.getY();
                if(status==MOVE_OUTSIDE){
                    startX=x;
                    startY=y;
                }else {
                    endX=x;
                    endY=y;
                    double length=changEndPoint(x,y);
                    if(length>maxLength){
                        goOut();
                        status=MOVE_OUTSIDE;
                    }
                }
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                goBack();
                //status=IDLE;
                break;
        }

        return true;
    }

    private void goBack(){
        PropertyValuesHolder endXHolder=PropertyValuesHolder.ofInt("endX",endX,startX);
        PropertyValuesHolder endYHolder=PropertyValuesHolder.ofInt("endY",endY,startY);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(this,endXHolder,endYHolder).setDuration(100);
        animator.setInterpolator(new OvershootInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status=IDLE;
                invalidate();
            }
        });
        animator.start();
    }

    private void goOut(){
        PropertyValuesHolder endXHolder=PropertyValuesHolder.ofInt("startX",startX,endX);
        PropertyValuesHolder endYHolder=PropertyValuesHolder.ofInt("startY",startY,endY);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(this,endXHolder,endYHolder).setDuration(100);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();

    }

    private double changEndPoint(int x,int y){
        double deltaX=x-startX;
        double deltaY=y-startY;
        double hypotenuse=Math.sqrt(deltaX*deltaX+deltaY*deltaY);
        sin=deltaY/hypotenuse;
        cos=deltaX/hypotenuse;
        currRadius= (int) ((1-hypotenuse/maxLength)*originRadius);
        currRadius=Math.max(5,currRadius);
        return hypotenuse;
    }

    private void changeStartPoint(int x,int y){
        double deltaX=endX-x;
        double deltaY=endY-y;
        double hypotenuse=Math.sqrt(deltaX*deltaX+deltaY*deltaY);
        sin=deltaY/hypotenuse;
        cos=deltaX/hypotenuse;
        currRadius= (int) ((1-hypotenuse/maxLength)*originRadius);
        currRadius=Math.max(5,currRadius);

    }

    private void  changeRadius(double length){

    }

    public void setEndX(int x){
        endX=x;

    }
    //TODO bad code
    public void setEndY(int y){
        endY=y;
        changEndPoint(endX,endY);
        invalidate();
    }

    public void setStartX(int x){
        startX=x;

    }
    public void setStartY(int y){
        startY=y;
        changeStartPoint(startX,startX);
        invalidate();
    }



    /************************************** explode **********************/


}
