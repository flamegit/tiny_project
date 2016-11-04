package com.flame.airplanemode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static com.flame.airplanemode.Utils.FINISH_HOUR;
import static com.flame.airplanemode.Utils.FINISH_MINUTE;
import static com.flame.airplanemode.Utils.START_HOUR;
import static com.flame.airplanemode.Utils.START_MINUTE;

public class MyService extends Service {

    public MyService() {
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name= "MainActivity";
        int sh,sm,fh,fm;
        sh = getSharedPreferences(name,MODE_PRIVATE).getInt(START_HOUR,22);
        sm = getSharedPreferences(name,MODE_PRIVATE).getInt(START_MINUTE,30);
        fh = getSharedPreferences(name,MODE_PRIVATE).getInt(FINISH_HOUR,8);
        fm= getSharedPreferences(name,MODE_PRIVATE).getInt(FINISH_MINUTE,30);
        Utils.scheduleAirPlane(this,sh,sm,fh,fm);
        Log.d("air",sh+":"+sm);
        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
