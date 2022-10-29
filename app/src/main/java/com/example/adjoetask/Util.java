package com.example.adjoetask;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;


public class Util {
    public static String timer;
    static Handler timerHandler = new Handler();
    static Runnable timerRunnable;
    static String BASE_URL = "https://jsonplaceholder.typicode.com/albums";

    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 1000);
        builder.setOverrideDeadline(3 * 1000);

        JobScheduler jobScheduler = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            jobScheduler = context.getSystemService(JobScheduler.class);
        }
        jobScheduler.schedule(builder.build());
    }

    public static void startTimerHandler() {
        getTimer();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public static void removeTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    public static void getTimer() {
        long startTime;
        startTime = System.currentTimeMillis();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = ((int) (millis / 1000)) / 60;
                seconds = seconds % 60;
                timer = "User  active time " + String.format("%d:%02d", minutes, seconds);
                timerHandler.postDelayed(this, 1000);
            }
        };

    }

}
