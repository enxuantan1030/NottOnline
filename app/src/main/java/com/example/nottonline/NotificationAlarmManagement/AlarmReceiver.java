package com.example.nottonline.NotificationAlarmManagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * The {@code AlarmReceiver} class is a {@code BroadcastReceiver} responsible for
 * receiving broadcast intents and triggering the {@code AlarmActivity} when an alarm
 * event occurs.
 */
public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Called when the {@code AlarmReceiver} receives a broadcast intent. Starts the
     * {@code AlarmActivity} to handle the alarm event.
     *
     * @param context The context in which the receiver is running.
     * @param intent  The {@code Intent} object containing information about the event.
     *                In this case, it is typically used to trigger the alarm.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive called");

        // Start the AlarmActivity
        Intent alarmActivityIntent = new Intent(context, AlarmActivity.class);
        alarmActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmActivityIntent);
    }
}

