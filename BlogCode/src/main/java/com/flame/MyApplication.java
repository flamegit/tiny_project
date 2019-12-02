package com.flame;

import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.flame.ui.MainActivity;

/**
 * Created by Administrator on 2016/12/1.
 */

public class MyApplication extends Application {

    private boolean isCalled=false;
    @Override
    public void onCreate() {

        super.onCreate();
        Log.d("fxlts","oncreate");
        PackageManager packageManager = getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            final AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            appOpsManager.startWatchingMode(AppOpsManager.OP_GET_USAGE_STATS, applicationInfo.packageName, new AppOpsManager.OnOpChangedListener()
            {
                @Override
                public void onOpChanged(String op, String packageName) {
                    if (!isCalled) {
                        Log.d("fxlts",op+packageName);
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    isCalled=true;
                }
            });
        }catch (PackageManager.NameNotFoundException e){
        }
    }
}
