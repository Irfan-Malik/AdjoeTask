package com.example.adjoetask;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyStartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Util.scheduleJob(context);
            Util.startTimerHandler();
        } else {
            Util.removeTimer();
        }

    }

}
