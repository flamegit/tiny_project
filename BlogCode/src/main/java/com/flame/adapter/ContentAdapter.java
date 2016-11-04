package com.flame.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class ContentAdapter extends PagerAdapter {
    List mCache;
    private int[] mPage;
    private String mContent;

    public ContentAdapter(int[] page, String content){
        mPage=page;
        mContent=content;
    }
    @Override
    public int getCount() {
        return mPage.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return  view==object;
    }
    private String getText(int page){
        if(page==0){
            return mContent.substring(0,mPage[0]);
        }
        return mContent.substring(mPage[page-1],mPage[page]);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TextView textView=null;
        if(mCache==null){
            mCache=new LinkedList();
        }
        if(mCache.size()>0){
            textView=(TextView) mCache.remove(0);
        }else {
            textView=new TextView(container.getContext());
        }
        textView.setText(getText(position));
        container.addView(textView);
        return textView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        mCache.add(object);
    }
}
