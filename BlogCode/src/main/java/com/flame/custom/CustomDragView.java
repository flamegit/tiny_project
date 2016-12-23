package com.flame.custom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
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
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Administrator on 2016/8/30.
 */
public class CustomDragView extends View {
    final static int IDLE=0;
    final static int INSIDE=1;
    final static int ANIMATION=2;
    final static int  EXPLODE=3;
    final static int  OVER=4;

    private static int CENTERX;
    private static int CENTERY;

    private int mTouchSlop;

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

    private int explodeRadius;
    private int explodePara;
    private int maxLength;

    Path path;
    int status;

    private String text="99";
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

        textPaint=new Paint();
        textPaint.setColor(Color.BLUE);
        textPaint.setTextSize(40);

        paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        currRadius=originRadius=30;
        maxLength=10*originRadius;

        path=new Path();
        status=IDLE;
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(status==INSIDE || status==ANIMATION){
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
            canvas.translate(endX-originRadius,endY-originRadius);
            canvas.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/2,getHeight()/2,paint);
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent, textPaint);
            canvas.restore();

//        }else if(status==OUTSIDE){
//            canvas.save();
//            canvas.translate(endX-originRadius,endY-originRadius);
//            canvas.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/2,getHeight()/2,paint);
//            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent, textPaint);
//            canvas.restore();
        } else if(status==EXPLODE){
            drawDotsFrame(canvas);
        }else if(status==OVER){
            return;
        }
        else {
            canvas.drawRoundRect(0,0,getWidth(),getHeight(),getHeight()/2,getHeight()/2,paint);
            canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent, textPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        CENTERX=startX=w/2;
        CENTERX=startY=h/2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                status=INSIDE;
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                endX=(int) event.getX();
                endY=(int) event.getY();
                if(Math.abs(endX-CENTERX)<mTouchSlop && Math.abs(endY -CENTERY) < mTouchSlop ){
                    return true;
                }
                if(status==OVER){
                    return true;
                }
                if(status==INSIDE){
                    boolean isOut=onPositionChange(endX-startX,endY-startY);
                    if(isOut){
                        status=ANIMATION;
                        goOut();
                    }
                }
                postInvalidate();
                break;

            case MotionEvent.ACTION_UP:
                if(status==INSIDE){
                    status=ANIMATION;
                    goBack();
                }
                break;
        }
        return true;
    }

    private void goBack(){
        PropertyValuesHolder endXHolder=PropertyValuesHolder.ofInt("endX",endX,startX);
        PropertyValuesHolder endYHolder=PropertyValuesHolder.ofInt("endY",endY,startY);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(this,endXHolder,endYHolder).setDuration(100);
        animator.setInterpolator(new OvershootInterpolator(4));
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status=IDLE;
                postInvalidate();
            }
        });
        animator.start();
    }
    private void goOut(){
        PropertyValuesHolder endXHolder=PropertyValuesHolder.ofInt("startX",startX,endX);
        PropertyValuesHolder endYHolder=PropertyValuesHolder.ofInt("startY",startY,endY);
        ObjectAnimator animator=ObjectAnimator.ofPropertyValuesHolder(this,endXHolder,endYHolder).setDuration(100);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status=EXPLODE;
                postInvalidate();
            }
        });
        //animator.start();
        ObjectAnimator explodeAnimation=ObjectAnimator.ofInt(this,"explodePara",1,10).setDuration(500);
        explodeAnimation.setInterpolator(new AccelerateInterpolator());
        explodeAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status=OVER;
                postInvalidate();
            }
        });
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playSequentially(animator,explodeAnimation);
        animatorSet.start();

    }
    private boolean onPositionChange(int deltaX,int deltaY){
        double hypotenuse=Math.sqrt(deltaX*deltaX + deltaY*deltaY);
        changeRadius(hypotenuse);
        sin=deltaY/hypotenuse;
        cos=deltaX/hypotenuse;
        if(hypotenuse>maxLength) {
            return true;
        }
        return false;
    }

    private void  changeRadius(double length){
        currRadius= (int) ((1-length/maxLength)*originRadius);
        currRadius=Math.max(5,currRadius);
    }

    public void setEndX(int x){
        endX=x;
    }

    //TODO bad code
    public void setEndY(int y){
        endY=y;
        onPositionChange(endX-startX,endY-startY);
        postInvalidate();
    }

    public void setStartX(int x){
        startX=x;
    }
    public void setStartY(int y){
        startY=y;
        onPositionChange(endX-startX,endY-startY);
        postInvalidate();
    }

    public void setExplodePara(int para){
        explodeRadius=50+10*para;
        paint.setAlpha(255-25*para);
        postInvalidate();
    }

    /************************************** explode **********************/

    private void drawDotsFrame(Canvas canvas) {
        for (int i = 0; i < 12; i++) {
            int cX = (int) (endX + explodeRadius * Math.cos((i * 30) * Math.PI / 180));
            int cY = (int) (endY + explodeRadius * Math.sin((i * 30 ) * Math.PI / 180));
            canvas.drawCircle(cX, cY, 10, paint);
        }
    }

}
