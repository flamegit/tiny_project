package com.flame.Utils;

import android.graphics.Rect;
import android.text.StaticLayout;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/8/23.
 */
public class Util {

    private static final int SIZE=5120;
    public String mContent;
    String mLeftContent;

    public void loadContent(String path){
        File file=new File(path);
        char[] buffer=new char[5120];
        try {
            FileInputStream inputStream=new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(inputStream);
            StringBuilder sb=new StringBuilder();
            int num ;
            while (true) {
                num = reader.read(buffer);
                if (num == -1) {
                    break;
                }
                sb.append(buffer);
            }
            mContent=sb.toString();
        }catch (IOException e){
            Log.d("MainActivity",e.toString());
        }
    }

    public void loadContent(InputStream inputStream){
        char[] buffer=new char[5120];
        try {
            InputStreamReader reader = new InputStreamReader(inputStream);
            StringBuilder sb=new StringBuilder();
            int num ;
            while (true) {
                num = reader.read(buffer);
                if (num == -1) {
                    break;
                }
                sb.append(buffer);
            }
            mContent=sb.toString();
        }catch (IOException e){
            Log.d("MainActivity",e.toString());
        }
    }





    private String getContent(int i){
        if(mContent==null ||mContent.length()==0)
            return null;
        int index=(i+1)*SIZE;
        if(mContent.length()<(i+1)*SIZE){
            index=mContent.length();
        }
        return mContent.substring(i*SIZE,index);
    }

    public int[] getPage( TextView textView){
        textView.setText(mContent);
        int count=textView.getLineCount();
        int pCount=getPageLineCount(textView);
        int pageNum=count/pCount;
        int page[]=new int[pageNum];
        for(int i=0;i<pageNum;i++){
            page[i]=textView.getLayout().getLineEnd((i+1)*pCount-1);
        }
        return page;
    }

    private int getPageLineCount(TextView view){
        /*
        * The first row's height is different from other row.
         */
        int h=view.getBottom()-view.getTop()-view.getPaddingTop();
        int firstH=getLineHeight(0,view);
        int otherH=getLineHeight(1,view);
        return (h-firstH)/otherH + 1 ;
    }
    private int getLineHeight(int line,TextView view){
        Rect rect=new Rect();
        view.getLineBounds(line,rect);
        return rect.bottom-rect.top;
    }

}
