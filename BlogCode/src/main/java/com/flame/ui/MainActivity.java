package com.flame.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.*;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;

import com.example.test.R;
import com.flame.adapter.FragmentAdapter;
import com.flame.adapter.RecyclerViewAdapter;
import com.flame.custom.ItemDecoration;

public class MainActivity extends AppCompatActivity {

    RecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            //window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout mTabLayout = (TabLayout)findViewById(R.id.tab_layout);
        ViewPager viewPager=(ViewPager)findViewById(R.id.view_pager);
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.select_tab1));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.select_tab2));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.select_tab3));
        //mTabLayout.setupWithViewPager();

        Fragment[] fragments=new Fragment[3];
        fragments[0]=new ContentFragment();
        fragments[1]=new SettingFragment();
        fragments[2]=new SettingFragment();
        FragmentAdapter adapter=new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
       // mTabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
