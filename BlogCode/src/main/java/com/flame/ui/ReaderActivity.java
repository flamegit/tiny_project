package com.flame.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.test.R;
import com.flame.Utils.Util;
import com.flame.adapter.ContentAdapter;
import com.flame.custom.CustomTextView;

import java.io.IOException;
import java.io.InputStream;

public class ReaderActivity extends AppCompatActivity implements CustomTextView.OnSizeChangeCallBack{

    CustomTextView mView;
    ViewPager mPager;
    View mProgress;
    String path= null;
    boolean hasData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        mView=(CustomTextView)findViewById(R.id.content);
        mView.setCallBack(this);
        mPager=(ViewPager)findViewById(R.id.pager);
        mProgress=findViewById(R.id.progress);
        Intent intent=getIntent();
        if(intent!=null){
            Uri uri=intent.getData();
            if(uri!=null){
                path=uri.getPath();
                hasData=true;
                Log.d("TAG",path);
            }
        }
    }

    private class LoadTask extends AsyncTask<String,String,Void> {
        Util util;
        @Override
        protected Void doInBackground(String... strings) {
            util = new Util();
            if(strings!=null&&strings.length>0&&hasData){
                util.loadContent(strings[0]);
            }else {
                try {
                    InputStream inputStream= getResources().getAssets().open("Activity.java");
                    util.loadContent(inputStream);
                    publishProgress();

                } catch (IOException e) {
                    e.printStackTrace();
                }
           }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            if (util != null) {
                Log.d("TAG",mView.getMeasuredHeight()+"");
                int[] pages = util.getPage(mView);
                PagerAdapter adapter = new ContentAdapter(pages, util.mContent);
                mPager.setAdapter(adapter);
                mPager.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                mView.setVisibility(View.GONE);
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }


    @Override
    public void onSizeChanged() {
        new LoadTask().execute(path);
    }
}
