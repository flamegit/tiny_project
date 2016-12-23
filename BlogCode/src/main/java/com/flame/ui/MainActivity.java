package com.flame.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.test.R;
import com.flame.NotificationReceiver;
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
        toolbar.setNavigationIcon(R.drawable.ic_chat_black_24dp);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            showNotification();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void showNotification() {
        Intent clickIntent = new Intent("action_click",null,getApplicationContext(), NotificationReceiver.class);
//      clickIntent.setAction("action_click");
        Intent dismissIntent = new Intent("action_dismiss", null, getApplicationContext(), NotificationReceiver.class);
//      intent.putExtra("realIntent", realIntent);
//      PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//              0);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this, 0, clickIntent,
                0);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent,
                0);

        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("测试标题")// 设置通知栏标题
                .setContentText("测试内容")// 设置通知栏显示内容
                // .setNumber(number);
                .setContentIntent(clickPendingIntent)
                .setDeleteIntent(dismissPendingIntent)
                .setTicker("测试通知来啦")// 通知栏首次出现在通知栏，带上动画效果
                .setWhen(System.currentTimeMillis())// 通知栏时间，一般是直接用系统的
                .setPriority(Notification.DEFAULT_ALL)// 设置通知栏优先级
                .setAutoCancel(true)// 用户单击面板后消失
                .setOngoing(false)// true,设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此
                // 占用设备(如一个文件下载，同步操作，主动网络连接)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，
                // 使用default属性，可以组合
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission

                .setSmallIcon(R.drawable.ic_launcher);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
//        notification.flags = Notification.FLAG_NO_CLEAR;// 点击清除的时候不清除
//      Intent realIntent = new Intent(getApplicationContext(), MainActivity.class);
//      realIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//      realIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        mNotificationManager.notify(0, mBuilder.build());
    }




}



