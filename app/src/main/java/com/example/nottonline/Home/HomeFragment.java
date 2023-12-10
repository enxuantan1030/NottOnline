package com.example.nottonline.Home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.nottonline.R;
import com.example.nottonline.Schedule.EditEventFragment;
import com.example.nottonline.Database.Event;
import com.example.nottonline.Database.EventAdapter;
import com.example.nottonline.Database.EventDatabase;
import com.example.nottonline.Schedule.ScheduleFragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * The {@code HomeFragment} class represents a fragment that displays a list of upcoming events
 * retrieved from a Room database. It includes functionality to navigate to the EditEventFragment
 * for editing selected events.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> events = new ArrayList<>(); // Initialize the events list
    private EventDatabase eventDB;

    /**
     * Default constructor for the HomeFragment.
     */
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of HomeFragment.
     *
     * @return A new instance of HomeFragment.
     */
    public static HomeFragment newInstance(int ID) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("Key",ID);
        homeFragment.setArguments(args);

        return homeFragment;
    }

    /**
     * Called when the fragment is created. Responsible for setting up the layout,
     * initializing UI components, and handling user interactions.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from
     *                           a previous saved state as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            recyclerView = rootView.findViewById(R.id.recyclerview);
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

            // Call the method to update RecyclerView data
            updateRecyclerViewData();
        }

        return rootView;
    }

    /**
     * Updates the data for the RecyclerView by querying the Room database
     * for the top 3 nearest events and updating the adapter.
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
            List<Event> eventsForDate = eventDB.getEventDao().getTop3NearestEvents(currentDate, currentTime, ID);

            // Update the RecyclerView data and notify the adapter on the UI thread
            requireActivity().runOnUiThread(() -> {
                // Ensure events list is initialized
                if (events == null) {
                    events = new ArrayList<>();
                }

                events.clear();
                events.addAll(eventsForDate);

                if (eventAdapter != null) {
                    eventAdapter.setEventList(events);
                    eventAdapter.notifyDataSetChanged();
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

    /**
     * Navigates to the EditEventFragment for editing the selected event.
     *
     * @param event The selected event to be edited.
     */
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
}
