package com.flame;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by AVAZUHOLDING on 2016/12/21.
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("action_dismiss")){
            Log.d("NotificaionReceiver","Notification delete");
        }
        if(intent.getAction().equals("action_click")){
            Log.d("NotificaionReceiver","Notification click");
        }
    }
}
