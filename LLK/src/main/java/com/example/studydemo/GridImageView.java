package com.example.studydemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import  static com.example.studydemo.Constants.COLUMN;
import  static com.example.studydemo.Constants.ROW;

import java.util.ArrayList;
import java.util.List;

class GridImageView extends View{
    private GameContract.Presenter mPresenter = null;
    private int  mSize;
    private Paint mBgPaint;
    private Paint mBoardPaint;
    private int mOffSetX;
    private int mOffSetY;
    private int[] mBoard;
    private List<Integer> mLine = null;
    private int mSelected;
    private Bitmap[] mBitmaps;

    public GridImageView(Context context) {
        super(context);
    }

    public GridImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.GridImageView);
        a.recycle();
        mBgPaint = new Paint();
        mBgPaint.setColor(Color.BLACK);
        mBoardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBoardPaint.setStyle(Paint.Style.STROKE);
        mBoardPaint.setStrokeWidth(10);
        mBoardPaint.setColor(Color.BLUE);
        mSelected=-1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSize=Math.min(w/COLUMN,h/ROW);
        mOffSetX=(w-COLUMN*mSize)/2;
        mOffSetY=(h-ROW*mSize)/2;
        mBitmaps=loadImages(mSize);
    }

    public void setPresenter(GameContract.Presenter presenter){
        mPresenter=presenter;
    }

    public void setGameBoard(int[] board){
        mBoard=board;
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int column = (x - mOffSetX) / mSize;
            int row = (y - mOffSetY) / mSize;
            if(row==0||row==ROW-1||column==0||column==COLUMN-1){
                return true;
            }
            if(mSelected==-1){
                mSelected=num(row,column);
            }else {
                checkLinked(num(row,column));
            }
            invalidate();
            return true;
        }
        return true;
    }

    private void checkLinked(int p){
        if(p==mSelected){
            return;
        }
        mLine=mPresenter.isLinked(mSelected,p);
        if(mLine==null){
            mSelected=p;
        }else {
            mSelected=-1;
            //TODO bad code
            postDelayed(new Runnable() {
                boolean isCleared=false;
                int p1,p2;
                @Override
                public void run() {
                    if(isCleared){
                        mPresenter.arrange(p1, p2);
                        invalidate();
                        return;
                    }
                    if (mLine != null) {
                        p1=mLine.get(0);
                        p2=mLine.get(mLine.size() - 1);
                        clear(p1, p2);
                        mLine = null;
                        invalidate();
                        isCleared=true;
                        postDelayed(this,100);
                    }
                }
            },100);

        }
    }

    private void clear(int i,int j){
        mBoard[i]=0;
        mBoard[j]=0;
    }

    private int row(int num) {
        return num / COLUMN;
    }
    private int column(int num) {
        return num % COLUMN;
    }
    private int num(int r, int c) {
        return r * COLUMN + c;
    }
    private int pointX(int p) {
        int c = column(p);
        return mOffSetX + c * mSize;
    }
    private int pointY(int p) {
        int r = row(p);
        return mOffSetY + r * mSize;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mBgPaint);
        drawBoard(canvas, mBoardPaint);
        drawLine(canvas, mBoardPaint);
        drawSelect(canvas,mBoardPaint);
    }

    private void drawBoard(Canvas canvas,Paint paint){
        if(mBoard==null){
            return;
        }
        for (int i = 0; i < COLUMN * ROW; i++) {
            if (getItem(i) != null) {
                canvas.drawBitmap(getItem(i), pointX(i), pointY(i), null);
            }
        }
    }

    private   void drawLine(Canvas c,Paint paint) {
        if (mLine == null)
            return;
        List<Integer> points = new ArrayList<Integer>();
        for (int i = 0; i < mLine.size(); i++) {
            points.add(pointX(mLine.get(i))+ mSize / 2);
            points.add(pointY(mLine.get(i))+ mSize / 2);
            points.add(pointX(mLine.get(i))+ mSize / 2);
            points.add(pointY(mLine.get(i))+ mSize / 2);
        }
        for (int i = 2; i < points.size() - 2;) {
            c.drawLine(points.get(i++), points.get(i++), points.get(i++),
                    points.get(i++), paint);
        }
    }

    private void drawSelect(Canvas c,Paint paint){
        if(mSelected==-1){
            return;
        }
        c.drawRect(pointX(mSelected),pointY(mSelected),pointX(mSelected)+mSize,pointY(mSelected)+mSize,paint);
    }

    private  Bitmap[] loadImages(int size) {
        int[] bitmapIds = {R.mipmap.a, R.mipmap.a, R.mipmap.b,
                R.mipmap.c, R.mipmap.d, R.mipmap.e, R.mipmap.f,
                R.mipmap.g, R.mipmap.h, R.mipmap.i, R.mipmap.j};
        Bitmap[] bitmaps = new Bitmap[bitmapIds.length];
        bitmaps[0] = null;
        for (int i = 1; i < bitmapIds.length; i++) {
            Drawable drawable =getResources().getDrawable(bitmapIds[i]);
            drawable.setBounds(1, 1, size - 1, size - 1);
            Bitmap b = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            drawable.draw(c);
            bitmaps[i] = b;
        }
        return bitmaps;
    }
    private Bitmap getItem(int i) {
        return mBitmaps[mBoard[i]];
    }

}
