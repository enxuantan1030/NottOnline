package com.example.nottonline.NotificationAlarmManagement;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.nottonline.Home.HomeFragment;
import com.example.nottonline.R;
import com.example.nottonline.Database.Event;
import com.example.nottonline.Schedule.ScheduleFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for scheduling and managing alarms and notifications related to events.
 */
public class CommonMethods {

    /**
     * Creates the notification channel for reminders.
     */
    public static void createNotificationChannel(String CHANNEL_ID, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminder Channel";
            String description = "Channel for event reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    /**
     * Schedules an alarm for a specific event.
     *
     * @param context  The application context.
     * @param event    The event for which the alarm is scheduled.
     * @param alarmID  The ID for the alarm.
     */
    public static void scheduleAlarm(Context context, Event event, int alarmID) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            Date dateTime = dateFormat.parse(event.getDate() + " " + event.getTime());

            long notificationTime = dateTime.getTime();

            Intent notificationIntent = new Intent(context, AlarmReceiver.class);
            notificationIntent.putExtra("title", event.getTitle());
            notificationIntent.putExtra("eventId", event.getId()); // Pass the event ID
            alarmID = event.getId();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    alarmID,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);

            Toast.makeText(context, "Alarm scheduled", Toast.LENGTH_SHORT).show();

            Log.d("Debug", "Alarm Set ");
            Log.d("Debug", "title:" + event.getTitle());
            Log.d("Debug", "time Set " + event.getTime() + " id: " + event.getId());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Schedules a notification for a specific event.
     *
     * @param context        The application context.
     * @param event          The event for which the notification is scheduled.
     * @param notificationID The ID for the notification.
     */
    public static void scheduleNotification(Context context, Event event, int notificationID) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            Date dateTime = dateFormat.parse(event.getDate() + " " + event.getTime());

            long notificationTime = dateTime.getTime() - (10 * 60 * 1000);

            Intent notificationIntent = new Intent(context, NotificationReceiver.class);
            notificationIntent.putExtra("title", event.getTitle());
            notificationIntent.putExtra("eventId", event.getId()); // Pass the event ID
            notificationID = event.getId();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    notificationID,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);

            Toast.makeText(context, "Notification scheduled", Toast.LENGTH_SHORT).show();

            Log.d("Debug", "Notification Set ");
            Log.d("Debug", "title:" + event.getTitle());
            Log.d("Debug", "time Set " + event.getTime() + " id: " + event.getId());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the HomeFragment by replacing the current fragment.
     *
     * @param parentFragmentManager The FragmentManager of the parent activity.
     */
    public static void openHomeFragment(FragmentManager parentFragmentManager) {
        // Create a FragmentTransaction
        FragmentTransaction transaction = parentFragmentManager.beginTransaction();

        // Replace the current fragment with the HomeFragment
        transaction.replace(R.id.fragment_container, new HomeFragment());

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    /**
     * Displays a discard changes dialog with options to discard or keep changes.
     *
     * @param context              The application context.
     * @param parentFragmentManager The FragmentManager of the parent activity.
     */
    public static void showDiscardChangesDialog(Context context, FragmentManager parentFragmentManager) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Discard Changes?");
        builder.setMessage("Are you sure you want to discard changes?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, redirect to ScheduleFragment
                parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new ScheduleFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked No, do nothing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
