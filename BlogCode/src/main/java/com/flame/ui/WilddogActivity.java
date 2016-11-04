package com.flame.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.wilddog.client.ChildEventListener;
//import com.wilddog.client.DataSnapshot;
//import com.wilddog.client.ValueEventListener;
//import com.wilddog.client.Wilddog;
//import com.wilddog.client.WilddogError;

import com.example.test.R;
import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import dalvik.system.PathClassLoader;

public class WilddogActivity extends Activity {
    private static final String AppId="https://flame.wilddogio.com/";
    static final String TAG="ActivityB";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        Wilddog.setAndroidContext(this);
        Wilddog ref = new Wilddog(AppId);
        ref.setValue(30);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(WilddogError wilddogError) {
            }
        });
        ref.addValueEventListener(
                new ValueEventListener(){
                    public void onDataChange(DataSnapshot snapshot){
                        System.out.println(snapshot.getValue()); //打印结果 "hello world!!!"
                    }

                    public void onCancelled(WilddogError error){
                        if(error != null){
                            System.out.println(error.getCode());
                        }
                    }
                });
        Log.d("ActivityA",getApplicationInfo().sourceDir+":"+getApplicationInfo().dataDir);
        Log.d("ActivityA", getApplicationInfo().sharedLibraryFiles.toString()+"");
    }

    private void testStorage(){
        Log.d(TAG, getDir("bin", Context.MODE_PRIVATE).getAbsolutePath()); ///data/data/com.example.myapp/app_bin
        Log.d(TAG,getFilesDir().getAbsolutePath());                        ///data/data/com.example.myapp/files
        Log.d(TAG,getCacheDir().getAbsolutePath());                        ///data/data/com.example.myapp/cache
        Log.d(TAG,getObbDir().getAbsolutePath());                          ///storage/sdcard0/Android/obb/


        Log.d(TAG,getExternalCacheDir().getAbsolutePath());               ///storage/sdcard1/Android/data/com.example.myapp/cache
        Log.d(TAG,getExternalFilesDir("apk").getAbsolutePath());             ///storage/sdcard1/Android/data/com.example.myapp/files


        Log.d(TAG, Environment.getExternalStorageState());                //mounted

        Log.d(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());           ///storage/sdcard0
        Log.d(TAG, Environment.getDataDirectory().getAbsolutePath());                      ///data
        Log.d(TAG, Environment.getDownloadCacheDirectory().getAbsolutePath());             ///cache
        Log.d(TAG, Environment.getExternalStoragePublicDirectory("apk").getAbsolutePath());   ///storage/sdcard0
        Log.d(TAG, Environment.getRootDirectory().getAbsolutePath());
    }

    public void load(){
        DownloadManager downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(null);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalPublicDir("apk","temlates");
        // request.setDestinationInExternalFilesDir(this,)
        long reference = downloadManager.enqueue(request);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


