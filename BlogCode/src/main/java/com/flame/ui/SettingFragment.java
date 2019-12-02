package com.flame.ui;



import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.example.test.R;

import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private boolean getDataUSAGE(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            if (mode == AppOpsManager.MODE_ALLOWED){
               // return true;
            }
            Intent intent = new Intent("android.settings.USAGE_ACCESS_SETTINGS");
            startActivity(intent);
            return false;

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.drag_text_layout,container,false);
       // final View textView=view.findViewById(R.id.touchview);

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CustomAnaimation anaimation=new CustomAnaimation(0,50,textView.getWidth(),textView.getHeight());
//                anaimation.setDuration(1000);
//                textView.startAnimation(anaimation);
//            }
//        });
        return view;
    }


    static class CustomAnaimation extends  Animation{

        Camera mCamera;
        int mStart;
        int mEnd;
        int mCenterX;
        int mCenterY;
        public CustomAnaimation(int start,int end,int width,int height){
            mStart=start;
            mEnd=end;
            mCamera=new Camera();
            mCenterX=width/2;
            mCenterX=height/2;
        }
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            //super.applyTransformation(interpolatedTime, t);
            float value=(mEnd-mStart)*interpolatedTime+mStart;
            mCamera.translate(0,0,value);
            Matrix matrix=t.getMatrix();
            mCamera.getMatrix(matrix);
            matrix.preTranslate(-mCenterX, -mCenterX);
            matrix.postTranslate(mCenterX, mCenterX);
        }
    }

}
