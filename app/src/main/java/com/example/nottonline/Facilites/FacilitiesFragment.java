package com.example.nottonline.Facilites;

import android.os.Bundle;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.net.Uri;

import com.example.nottonline.Facilites.CourseChecker.CourseCheckerFragment;
import com.example.nottonline.R;

/**
 * The {@code FacilitiesFragment} class represents a fragment that provides buttons for various facilities.
 * Users can click on these buttons to open external websites or navigate to specific fragments.
 */
public class FacilitiesFragment extends Fragment {

    /**
     * Default constructor for the FacilitiesFragment.
     */
    public FacilitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of FacilitiesFragment.
     *
     * @return A new instance of FacilitiesFragment.
     */
    public static FacilitiesFragment newInstance() {
        return new FacilitiesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_facilities, container, false);

        // Add buttons to open websites or navigate to fragments
        AppCompatImageButton Button_blueCastle = rootView.findViewById(R.id.button_blue_castle);
        AppCompatImageButton Button_NottinghamHub = rootView.findViewById(R.id.button_nottinghamhub);
        AppCompatImageButton Button_SportComplex = rootView.findViewById(R.id.button_sport_complex);
        AppCompatImageButton Button_CourseChecker = rootView.findViewById(R.id.button_course_checker);

        // Set click listeners for each button
        Button_blueCastle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://bluecastle-my-results.nottingham.ac.uk/Account/Login?ReturnUrl=%2f");
            }
        });

        Button_NottinghamHub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://campus.nottingham.ac.uk/psp/csprd/?cmd=login");
            }
        });

        Button_SportComplex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebsite("https://apps.nottingham.edu.my/jw/web/userview/booking/v/_/home");
            }
        });

        Button_CourseChecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCourseCheckerFragment();
            }
        });

        return rootView;
    }

    /**
     * Opens a website using an Intent to view the specified URL.
     *
     * @param url The URL of the website to open.
     */
    private void openWebsite(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Opens the CourseCheckerFragment by replacing the current fragment with a new instance.
     */
    private void openCourseCheckerFragment() {
        // Create a FragmentTransaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the current fragment with the target fragment (CourseCheckerFragment)
        transaction.replace(R.id.fragment_container, new CourseCheckerFragment());

        // Add the transaction to the back stack (optional)
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
