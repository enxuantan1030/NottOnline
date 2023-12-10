package com.example.nottonline.Schedule;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.nottonline.R;
import com.example.nottonline.Database.Event;
import com.example.nottonline.Database.EventAdapter;
import com.example.nottonline.Database.EventDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ScheduleFragment extends Fragment {

    public ScheduleFragment() {
        // Required empty public constructor
    }
    CalendarView calendarView;
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    List<Event> events;
    EventDatabase eventDB;
    Calendar calendar;
    public static ScheduleFragment newInstance(int ID) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt("Key",ID);
        scheduleFragment.setArguments(args);
        return scheduleFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {

        recyclerView = rootView.findViewById(R.id.recyclerview);
        calendarView = rootView.findViewById(R.id.calendarView);

        // Initialize RecyclerView and its adapter
        events = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext(), events, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                navigateToEditPage(event);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(eventAdapter);

        // Initialize Room database
        eventDB = Room.databaseBuilder(requireContext(), EventDatabase.class, "EventDB")
                .fallbackToDestructiveMigration()
                .build();

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                    // Update RecyclerView data based on the selected date
                    String selectedDate = formatDate(year, month, day);
                    updateRecyclerViewData(selectedDate, rootView);
                }
            });

            // Handle the button click event
            FloatingActionButton button = rootView.findViewById(R.id.fab);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAddEventFragment();
                }
            });
            // Default: Show events for the current date
            String currentDate = getCurrentDate();
            updateRecyclerViewData(currentDate, rootView);
        }



        return rootView;
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return formatDate(year, month, day);
    }

    private void updateRecyclerViewData(String date, View rootView) {
        int ID = getArguments().getInt("Key", 0);
        // Run database query in the background thread
        AsyncTask.execute(() -> {
            List<Event> eventsForDate = eventDB.getEventDao().getEventsForDate(date, ID);

            // Update the RecyclerView data and notify the adapter on the UI thread
            requireActivity().runOnUiThread(() -> {
                events.clear();
                events.addAll(eventsForDate);
                eventAdapter.notifyDataSetChanged();

                // Toggle the visibility of the TextView based on whether there are events or not
                TextView textNoEvents = rootView.findViewById(R.id.textNoEvents);
                if (events.isEmpty()) {
                    textNoEvents.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    textNoEvents.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            });
        });
    }


    private String formatDate(int year, int month, int dayOfMonth) {

        return String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
    }

    private void navigateToEditPage(Event event) {

        EditEventFragment editEventFragment = new EditEventFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("event", (Serializable) event);
        editEventFragment.setArguments(bundle);

        // Start the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the current fragment with the EditEventFragment
        transaction.replace(R.id.fragment_container, editEventFragment);

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
    private void openAddEventFragment() {
        AddEventFragment addEventFragment = new AddEventFragment();

        int ID = getArguments().getInt("Key",0);
        Bundle bundle = new Bundle();
        bundle.putInt("Key", ID);

        addEventFragment.setArguments(bundle);

        // Create a FragmentTransaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the current fragment with the target fragment
        transaction.replace(R.id.fragment_container, addEventFragment);

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

}
