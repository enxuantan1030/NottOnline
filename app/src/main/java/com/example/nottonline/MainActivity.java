package com.example.nottonline;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.nottonline.Facilites.FacilitiesFragment;
import com.example.nottonline.Home.HomeFragment;
import com.example.nottonline.Login.ForgotPasswordActivity;
import com.example.nottonline.Login.LoginActivity;
import com.example.nottonline.Notifications.NotificationFragment;
import com.example.nottonline.Schedule.AddEventFragment;
import com.example.nottonline.Schedule.ScheduleFragment;

/**
 * The main activity for the application, responsible for managing the bottom navigation and loading fragments.
 */
public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        // Initialize and configure the bottom navigation bar
        setupBottomNavigation();

        // Retrieve user ID from the intent
        int userID = getIntent().getIntExtra("key", 0);

        // Set click listener for bottom navigation items
        bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1: // HOME
                    HomeFragment homeFragment = HomeFragment.newInstance(userID);
                    loadFragment(homeFragment);
                    break;
                case 2: // NOTIFICATION
                    NotificationFragment notificationFragment = NotificationFragment.newInstance(userID);
                    loadFragment(notificationFragment);
                    break;
                case 3: // FACILITIES
                    loadFragment(new FacilitiesFragment());
                    break;
                case 4: // SCHEDULE
                    ScheduleFragment scheduleFragment = ScheduleFragment.newInstance(userID);
                    loadFragment(scheduleFragment);
                    break;
                case 5: // EXIT
                    showExitConfirmationDialog();
                    break;
            }
            return null;
        });

        // Load the default fragment when the activity is created
        loadFragment(new HomeFragment());
    }

    /**
     * Sets up and configures the bottom navigation bar.
     */
    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.show(2, true);

        // Add icons for bottom navigation items
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.baseline_notifications_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.baseline_facilities));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.baseline_table_chart_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.baseline_exit_to_app_24));
    }

    /**
     * Displays an exit confirmation dialog when the user clicks on the "Exit" menu item.
     */
    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit App");
        builder.setMessage("Are you sure you want to exit the app?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked Yes, exit the app
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked No, dismiss the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Loads the specified fragment into the fragment container.
     *
     * @param fragment The fragment to be loaded.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

