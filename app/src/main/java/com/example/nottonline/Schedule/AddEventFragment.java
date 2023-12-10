package com.example.nottonline.Schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
import com.example.nottonline.Database.EventDatabase;
import com.example.nottonline.NotificationAlarmManagement.CommonMethods;
import com.google.android.material.textfield.TextInputEditText;
import com.example.nottonline.Database.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment for adding events to the schedule.
 */
public class AddEventFragment extends Fragment {

    // Notification channel ID for reminders
    public static final String CHANNEL_ID = "reminder";

    // Notification ID
    public static int notificationID;

    // Calendar instance for date and time selection
    final Calendar myCalendar = Calendar.getInstance();

    // UI elements
    TextInputEditText input_time, input_date, input_desc, input_title;
    ImageButton button_add_event;
    CheckBox alarm, notification;
    boolean isAlarmEnabled = false;
    boolean isNotificationEnabled = false;

    // EventDatabase instance
    EventDatabase eventDB;

    /**
     * Creates a new instance of AddEventFragment.
     *
     * @return A new instance of AddEventFragment.
     */
    public AddEventFragment() {
        // Required empty public constructor
    }


    public static AddEventFragment newInstance() {
        return new AddEventFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v1 = inflater.inflate(R.layout.fragment_add_event, container, false);

        // Create the notification channel
        CommonMethods.createNotificationChannel(CHANNEL_ID, requireContext());

        // Initialize UI elements
        input_date = v1.findViewById(R.id.input_date);
        input_time = v1.findViewById(R.id.input_time);
        selectDate();

        input_time.setOnClickListener(v->selectTime());

        input_desc = v1.findViewById(R.id.input_desc);
        input_title = v1.findViewById(R.id.input_title);

        button_add_event = v1.findViewById(R.id.button_add_event);

        alarm = v1.findViewById(R.id.checkBox);
        notification = v1.findViewById(R.id.checkBox2);

        // Set up RoomDatabase
        setupRoomDatabase();

        // Set up checkbox listeners
        setupCheckBoxListeners();

        // Set up back button listener
        setupBackButton(v1);

        // Set up add event button listener
        setupAddEventButton();

        return v1;
    }

    /**
     * Sets up RoomDatabase for handling events.
     */
    private void setupRoomDatabase() {
        RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        eventDB = Room.databaseBuilder(requireContext(),EventDatabase.class,
                "EventDB").addCallback(myCallback).build();
    }

    /**
     * Sets up checkbox listeners for alarm and notification.
     */
    private void setupCheckBoxListeners() {
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
    }

    /**
     * Sets up the back button listener.
     */
    private void setupBackButton(View view) {
        ImageButton backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showDiscardChangesDialog(requireContext(),getParentFragmentManager());
            }
        });
    }

    /**
     * Sets up the add event button listener.
     */
    private void setupAddEventButton() {
        button_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = input_title.getText().toString();
                String desc = input_desc.getText().toString();
                String date = input_date.getText().toString();
                String time = input_time.getText().toString();
                int ID = getArguments().getInt("Key", 0);

                // Set the values based on checkbox states
                boolean alarmValue = isAlarmEnabled;
                boolean notificationValue = isNotificationEnabled;

                String notificationMessage = null;
                if (isNotificationEnabled) {
                    notificationMessage = "Reminder: " + input_title.getText().toString() + " in 10 minutes";
                }

                Event event = new Event(title, desc, date, time, ID, alarmValue, notificationValue, notificationMessage);

                addEventInBackground(event);
            }
        });
    }


    /**
     * Initializes UI elements and listeners for selecting a date.
     */
    private void selectDate(){
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR,year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

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


    /**
     * Updates the selected date in the UI.
     *
     * @return A formatted date string.
     */
    private String updateDate(){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        return dateFormat.format(myCalendar.getTime());
    }

    /**
     * Initializes UI elements and listeners for selecting a time.
     */
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
     * Adds an event to the database in the background.
     *
     * @param event The event to be added.
     */
    public void addEventInBackground(Event event){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Get the current time
                Calendar currentTime = Calendar.getInstance();
                int currentYear = currentTime.get(Calendar.YEAR);
                int currentDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);

                // Parse the user input date and time
                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
                Date dateTime = null;
                try {
                    dateTime = dateTimeFormat.parse(event.getDate() + " " + event.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                    // background task
                    eventDB.getEventDao().addEvent(event);

                    // Retrieve the last inserted ID using a query
                    int eventId = eventDB.getEventDao().getLastInsertedEventId();

                    // Update the event with the assigned ID
                    event.setId(eventId);

                    // on finishing task
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Successfully Add Event", Toast.LENGTH_SHORT).show();

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



}
