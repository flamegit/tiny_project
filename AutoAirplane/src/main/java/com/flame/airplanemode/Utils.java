package com.flame.airplanemode;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;

import static android.provider.Settings.Global.AIRPLANE_MODE_ON;

/**
 * Created by Administrator on 2016/11/1.
 */

public class Utils {

    public static final String START_HOUR="start_hour";
    public static final String START_MINUTE="start_minute";
    public static final String FINISH_HOUR="finish_hour";
    public static final String FINISH_MINUTE="finish_minute";
    public static final String SWITCH="switch";

    static void setAirPlane(Context context,boolean enable){
        if(isAirplaneModeOn(context)==enable) {
            return;
        }
        try {
            Process process = Runtime.getRuntime().exec("su");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            if(enable){
                bw.write("settings put global airplane_mode_on 1;");
            }else {
                bw.write("settings put global airplane_mode_on 0;");
            }
            bw.write("\n");
            bw.flush();
            bw.write("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state "+enable+";");
            bw.write("\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isAirplaneModeOn(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            return Settings.System.getInt(contentResolver, AIRPLANE_MODE_ON, 0) != 0;
        } catch (NullPointerException e) {
            // https://github.com/square/picasso/issues/761, some devices might crash here, assume that
            // airplane mode is off.
            return false;
        } catch (SecurityException e) {
            //https://github.com/square/picasso/issues/1197
            return false;
        }
    }

    static void scheduleAirPlane(Context context,int sh,int sm,int fh,int fm) {
        long curr = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(curr);
        calendar.set(Calendar.HOUR_OF_DAY, sh);
        calendar.set(Calendar.MINUTE, sm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY,fh);
        calendar.set(Calendar.MINUTE, fm);
        long finish = calendar.getTimeInMillis();
        if(start==finish) return;

        long nextTime = start;
        boolean isAirPlaneMod=isAirplaneModeOn(context);
        if(start<finish){
            if (curr < start) {
                setAirPlane(context,false);
                nextTime = start;
            } else if (curr < finish) {
                setAirPlane(context,true);
                nextTime = finish;
            } else if (curr > finish) {
                setAirPlane(context,false);
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                calendar.set(Calendar.HOUR_OF_DAY, sh);
                calendar.set(Calendar.MINUTE, sm);
                nextTime = calendar.getTimeInMillis();
            }
        }else {
            if (curr < finish) {
                setAirPlane(context,true);
                nextTime = finish;
            } else if (curr < start) {
                setAirPlane(context,false);
                nextTime = start;
            } else if (curr > start) {
                setAirPlane(context,true);
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
                calendar.set(Calendar.HOUR_OF_DAY, fh);
                calendar.set(Calendar.MINUTE, fm);
                nextTime = calendar.getTimeInMillis();
            }
        }
        Intent alarmIntent = new Intent(context, MyService.class);
        PendingIntent intent = PendingIntent.getService(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, nextTime, intent);
    }

     static void disable(Context context){
        Intent alarmIntent = new Intent(context, MyService.class);
        PendingIntent intent = PendingIntent.getService(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(intent);
    }

}
