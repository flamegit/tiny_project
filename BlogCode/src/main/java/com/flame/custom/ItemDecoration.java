package com.flame.custom;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/9/21.
 */

public class ItemDecoration extends RecyclerView.ItemDecoration{


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(10,10,10,10);
        super.getItemOffsets(outRect, view, parent, state);
    }
}
