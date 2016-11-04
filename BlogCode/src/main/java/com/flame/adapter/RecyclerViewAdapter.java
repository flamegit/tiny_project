package com.flame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
       return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==FOOTER){
            ((TextView) holder.itemView).setText("Bottom");
        }else {
            ((TextView) holder.itemView).setText(""+mlists.get(position));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getDefault().post(new RxBus.ItemEvent());
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
