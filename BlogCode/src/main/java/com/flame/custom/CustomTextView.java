package com.flame.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class CustomTextView extends TextView {


    public  interface  OnSizeChangeCallBack{
        public void onSizeChanged();
    }

    OnSizeChangeCallBack callBack;

    public void setCallBack(OnSizeChangeCallBack callBack){
        this.callBack=callBack;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        callBack.onSizeChanged();

    }
}
