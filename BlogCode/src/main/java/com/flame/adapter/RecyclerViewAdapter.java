package com.flame.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.test.R;
import com.flame.Utils.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Integer> mlists;
    private static int FOOTER=1;
    private boolean isBottom;

    public RecyclerViewAdapter(){
        mlists=new ArrayList<>();
        for(int i=1;i<21;i++){
            mlists.add(i);
        }
    }

    public void refresh(){
        mlists.set(0,20);
        notifyItemChanged(0);
    }

    public void showFooter(){
        isBottom=true;
        notifyItemInserted(getItemCount());
    }

    @Override
    public int getItemViewType(int position) {
        if(position==mlists.size()){
            return FOOTER;
        }else {
            return 0;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drag_text_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("fxlts","onclick");

            }
        });
    }

    @Override
    public int getItemCount() {
        return isBottom ? mlists.size()+1:mlists.size();
    }

    public void remove(int position){
        mlists.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
