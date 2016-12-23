package com.flame.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.test.R;
import com.flame.adapter.RecyclerViewAdapter;
import com.flame.custom.ItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentFragment extends Fragment {

    RecyclerViewAdapter mAdapter;

    public ContentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.list_fragment,container,false);
       // SwipeRefreshLayout layout= (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh);
        //layout.setNestedScrollingEnabled(true);
        mAdapter=new RecyclerViewAdapter();
        final RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new ItemDecoration() );
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    mAdapter.showFooter();
                }
            }
        });
//        ItemTouchHelper touchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP,ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
////                mAdapter.onItemMove(viewHolder.getAdapterPosition(),
////                        target.getAdapterPosition());
//                return true;
//            }
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                mAdapter.remove(viewHolder.getAdapterPosition());
//            }
//
//        });
//        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mAdapter);
        return view;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mAdapter.refresh();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
