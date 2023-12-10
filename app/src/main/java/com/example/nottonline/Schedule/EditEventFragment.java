package com.example.nottonline.Schedule;

import static com.example.nottonline.NotificationAlarmManagement.CommonMethods.showDiscardChangesDialog;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.nottonline.R;
import com.example.nottonline.Database.Event;
import com.example.nottonline.Database.EventDatabase;
import com.example.nottonline.NotificationAlarmManagement.AlarmReceiver;
import com.example.nottonline.NotificationAlarmManagement.CommonMethods;
import com.example.nottonline.NotificationAlarmManagement.NotificationReceiver;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@code EditEventFragment} class is a Fragment responsible for editing and updating existing events.
 * It provides UI elements for modifying event details such as title, description, date, time, alarm, and notification.
 * Users can save changes, delete events, and set alarms or notifications for their events.
 */
public class EditEventFragment extends Fragment {

    TextInputEditText input_title, input_desc, input_date, input_time;
    CheckBox alarm, notification;
    final Calendar myCalendar = Calendar.getInstance();
    EventDatabase eventDB;

    ImageButton button_save_changes_event, button_delete;
    boolean isAlarmEnabled = false;
    boolean isNotificationEnabled = false;

    /**
     * Static method to create a new instance of {@code EditEventFragment}.
     *
     * @return A new instance of {@code EditEventFragment}.
     */
    public static EditEventFragment newInstance() {
        return new EditEventFragment();
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views.
     * @param container          This is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_event, container, false);


        input_title = rootView.findViewById(R.id.input_title);
        input_desc = rootView.findViewById(R.id.input_desc);
        input_date = rootView.findViewById(R.id.input_date);
        input_time = rootView.findViewById(R.id.input_time);
        alarm = rootView.findViewById(R.id.checkBox);
        notification = rootView.findViewById(R.id.checkBox2);
        selectDate();

        input_time.setOnClickListener(v->selectTime());

        Event event = (Event) getArguments().getSerializable("event");
        if(event != null) {
            input_title.setText(event.getTitle());
            input_desc.setText(event.getDescription());
            input_date.setText(event.getDate());
            input_time.setText(event.getTime());
            alarm.setChecked(event.getAlarm());
            notification.setChecked(event.getNotification());

        }

        eventDB = Room.databaseBuilder(requireContext(), EventDatabase.class, "EventDB")
                .fallbackToDestructiveMigration()
                .build();

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAlarmEnabled = isChecked;
            }
        });

        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNotificationEnabled = isChecked;
            }
        });

        button_save_changes_event = rootView.findViewById(R.id.button_save_changes_event);
        button_delete = rootView.findViewById(R.id.button_delete);

        ImageButton backButton = rootView.findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiscardChangesDialog(requireContext(),getParentFragmentManager());
            }
        });

        button_save_changes_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int UID = event.getId();
                String title = input_title.getText().toString();
                String desc = input_desc.getText().toString();
                String date = input_date.getText().toString();
                String time = input_time.getText().toString();
                int ID = event.getUser_ID();
                boolean alarmValue = isAlarmEnabled;
                boolean notificationValue = isNotificationEnabled;

                String notificationMessage = null;
                if (isNotificationEnabled) {
                    notificationMessage = "Reminder: " + input_title.getText().toString() + " in 10 minutes";
                }

                // Perform database query on a background thread
                String finalNotificationMessage = notificationMessage;

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        Event existingEvent = eventDB.getEventDao().getEventById(UID);

                        // Update the fields
                        existingEvent.setTitle(title);
                        existingEvent.setDescription(desc);
                        existingEvent.setDate(date);
                        existingEvent.setTime(time);
                        existingEvent.setAlarm(alarmValue);
                        existingEvent.setNotification(notificationValue);
                        existingEvent.setNotificationMessage(finalNotificationMessage);

                        // Perform the update on the main thread
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Perform the update on the main thread
                                editEventInBackground(existingEvent);
                            }
                        });
                    }

                });
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                int UID = event.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete Event?");
                builder.setMessage("Are you sure you want to delete this event?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Perform database delete on a background thread
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                // Fetch the existing event from the database
                                Event existingEvent = eventDB.getEventDao().getEventById(UID);

                                if (existingEvent != null) {
                                    // Perform the delete operation
                                    eventDB.getEventDao().deleteEvent(existingEvent);

                                    // Perform the delete on the main thread
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Notify the user about the deletion
                                            CommonMethods.openHomeFragment(getParentFragmentManager());
                                            Toast.makeText(getContext(), "Event Deleted!", Toast.LENGTH_SHORT).show();

                                            // Optionally, you can navigate back or perform any other UI update
                                        }
                                    });
                                }
                            }
                        });
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
        });

        return rootView;
    }

    private void selectDate() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                input_date.setText(updateDate());

            }
        };
        input_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireContext(), date, myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }
    private void selectTime(){
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minut = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minut) {
                currentTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                currentTime.set(Calendar.MINUTE,minut);

                String myFormat = "HH:mm";
                SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat,Locale.US);
                input_time.setText(dateFormat.format(currentTime.getTime()));
            }
        },hour,minut,true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    /**
     * Execute the background task for editing an event.
     *
     * @param event The Event object to be edited and updated.
     */
    public void editEventInBackground(Event event) {
        if (event == null) {
            // Handle the case where the event is null
            Log.e("Error", "Event is null");
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                    // Cancel existing alarms and notifications if the checkboxes are unchecked
                    if (!event.getAlarm() && !isAlarmEnabled) {
                        cancelAlarm(event.getId());
                    }

                    if (!event.getNotification() && !isAlarmEnabled) {
                        cancelNotification(event.getId());
                    }

                    // background task
                    eventDB.getEventDao().updateEvent(event);

                    // on finishing task
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Successfully Edited Event!", Toast.LENGTH_SHORT).show();

                            // Schedule alarm if the checkbox is checked
                            if (isAlarmEnabled) {
                                Log.d("Debug", "Alarm Set " + true);
                                CommonMethods.scheduleAlarm(requireContext(), event, event.getId());
                            }

                            // Schedule notification if the checkbox is checked
                            if (isNotificationEnabled) {
                                Log.d("Debug", "Notification Set " + true);
                                CommonMethods.scheduleNotification(requireContext(), event, event.getId());
                            }

                            CommonMethods.openHomeFragment(getParentFragmentManager());
                        }
                    });

            }
        });
    }

    /**
     * Cancel a scheduled notification.
     *
     * @param notificationId The ID of the notification to be canceled.
     */
    private void cancelNotification(int notificationId) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent notificationIntent = new Intent(requireContext(), NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        requireContext(),
                        notificationId,
                        notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                Toast.makeText(requireContext(), "Notification canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Cancel a scheduled alarm.
     *
     * @param alarmId The ID of the alarm to be canceled.
     */
    private void cancelAlarm(int alarmId) {
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent alarmIntent = new Intent(requireContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        requireContext(),
                        alarmId,
                        alarmIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                Toast.makeText(requireContext(), "Alarm canceled", Toast.LENGTH_SHORT).show();
            }
        });
    }

}