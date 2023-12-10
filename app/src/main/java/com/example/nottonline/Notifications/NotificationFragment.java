package com.example.nottonline.Notifications;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nottonline.Home.HomeFragment;
import com.example.nottonline.R;
import com.example.nottonline.Database.Event;
import com.example.nottonline.Database.EventDatabase;
import com.example.nottonline.Schedule.ScheduleFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * The {@code NotificationFragment} class is a Fragment that displays a list of notification events
 * using a RecyclerView. The notification data is retrieved from a Room database and updated in the
 * background thread to ensure responsiveness.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Event> events = new ArrayList<>(); // Initialize the events list
    private EventDatabase eventDB;

    /**
     * Default constructor for the {@code NotificationFragment}.
     */
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Static method to create a new instance of {@code NotificationFragment}.
     *
     * @return A new instance of {@code NotificationFragment}.
     */
    public static NotificationFragment newInstance(int ID) {
        NotificationFragment notificationFragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt("Key",ID);
        notificationFragment.setArguments(args);
        return notificationFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {

            recyclerView = rootView.findViewById(R.id.recyclerview);
            notificationAdapter = new NotificationAdapter(getContext(), events);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(notificationAdapter);

            // Initialize Room database
            eventDB = Room.databaseBuilder(requireContext(), EventDatabase.class, "EventDB")
                    .fallbackToDestructiveMigration()
                    .build();

            // Call the method to update RecyclerView data
            updateRecyclerViewData();
        }
        return rootView;
    }

    /**
     * Update the RecyclerView data by retrieving notification history from the Room database
     * and updating the UI on the main thread.
     */
    private void updateRecyclerViewData() {
        // Run database query in the background thread
        AsyncTask.execute(() -> {
            // Get the current date and time
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
            String currentDate = dateFormat.format(calendar.getTime());
            String currentTime = timeFormat.format(calendar.getTime());

            int ID = getArguments().getInt("Key", 0);

            // Call the Room query to get the top 3 nearest events
            List<Event> notificationHistory = eventDB.getEventDao().notificationHistory(currentDate, currentTime,ID);

            // Update the RecyclerView data and notify the adapter on the UI thread
            requireActivity().runOnUiThread(() -> {
                // Ensure events list is initialized
                if (events == null) {
                    events = new ArrayList<>();
                }

                events.clear();
                events.addAll(notificationHistory);

                if (notificationAdapter != null) {
                    notificationAdapter.setNotificationList(events);
                    notificationAdapter.notifyDataSetChanged();
                }

                // Toggle visibility of "No Events" TextView based on the presence of events
                TextView textNoEvents = requireView().findViewById(R.id.textNoEvents);
                if (events.isEmpty()) {
                    textNoEvents.setVisibility(View.VISIBLE);
                } else {
                    textNoEvents.setVisibility(View.GONE);
                }
            });
        });
    }
}
