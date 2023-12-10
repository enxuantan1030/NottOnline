package com.example.nottonline.NotificationAlarmManagement;

import static com.example.nottonline.Schedule.AddEventFragment.CHANNEL_ID;
import static com.example.nottonline.Schedule.AddEventFragment.notificationID;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.nottonline.R;
import com.example.nottonline.Schedule.AddEventFragment;

/**
 * The {@code NotificationReceiver} class is a {@code BroadcastReceiver} responsible for
 * receiving broadcast intents triggered by scheduled notifications and displaying them
 * as system notifications.
 */
public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Called when the {@code NotificationReceiver} receives a broadcast intent. Retrieves
     * the notification title from the intent and displays a system notification with the
     * specified title.
     *
     * @param context The context in which the receiver is running.
     * @param intent  The {@code Intent} object containing information about the event.
     *                In this case, it includes the notification title.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver", "onReceive called");

        // Retrieve notification title from the intent
        String title = intent.getStringExtra("title");

        // Display the notification
        showNotification(context, title);
    }

    /**
     * Displays a system notification with the specified title.
     *
     * @param context The context in which the notification is created.
     * @param title   The title to be displayed in the notification.
     */
    private void showNotification(Context context, String title) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create an intent to be triggered when the notification is tapped
        Intent resultIntent = new Intent(context, AddEventFragment.class); // Replace AddEventFragment with the actual activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Build the notification
        Notification notification = new NotificationCompat.Builder(context, AddEventFragment.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText("Event in 10 minutes!")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        // Display the notification
        notificationManager.notify(AddEventFragment.notificationID, notification);
    }
}
