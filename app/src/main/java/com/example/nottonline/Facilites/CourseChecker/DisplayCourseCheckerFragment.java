package com.example.nottonline.Facilites.CourseChecker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.nottonline.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Fragment for displaying and filtering course information based on various criteria.
 * The fragment includes Spinners for module, type, location, and staff selection.
 * Users can apply filters and view organized course data.
 *
 */
public class DisplayCourseCheckerFragment extends Fragment {

    private List<List<String>> columnGroups;
    private Spinner moduleSpinner;
    private Spinner typeSpinner;
    private Spinner locationSpinner;
    private Spinner staffSpinner;
    private LinearLayout lineardisplay;

    private boolean[] selectedModules;
    private boolean[] selectedTypes;
    private boolean[] selectedLocations;
    private boolean[] selectedStaffs;

    /**
     * Constructs a new DisplayCourseCheckerFragment with the provided column groups.
     *
     * @param columnGroups List of column groups containing course information.
     */
    public DisplayCourseCheckerFragment(List<List<String>> columnGroups) {
        this.columnGroups = columnGroups;
    }

    /**
     * Initializes the fragment's UI components, including Spinners and apply button.
     * Displays organized course data based on columnGroups, if available.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return The root view of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display_course_checker, container, false);

        // Initialize lineardisplay
        lineardisplay = rootView.findViewById(R.id.lineardisplay);
        // Initialize Spinners
        moduleSpinner = rootView.findViewById(R.id.moduleSpinner);
        typeSpinner = rootView.findViewById(R.id.typeSpinner);
        locationSpinner = rootView.findViewById(R.id.locationSpinner);
        staffSpinner = rootView.findViewById(R.id.staffSpinner);

        // Set up Spinners with dynamic options
        setupSpinner(moduleSpinner, extractUniqueOptions(1), "Module");
        setupSpinner(typeSpinner, extractUniqueOptions(2),"Type");
        setupSpinner(locationSpinner, extractUniqueOptions(9),"Location");
        setupSpinner(staffSpinner, extractUniqueOptions(11),"Staff");

        // Initialize boolean arrays for multi-selection
        selectedModules = new boolean[moduleSpinner.getCount()];
        selectedTypes = new boolean[typeSpinner.getCount()];
        selectedLocations = new boolean[locationSpinner.getCount()];
        selectedStaffs = new boolean[staffSpinner.getCount()];

        // Set up onClickListener for each spinner to show multi-choice dialog
        setupMultiChoiceSpinner(moduleSpinner, selectedModules, "Select Modules");
        setupMultiChoiceSpinner(typeSpinner, selectedTypes, "Select Types");
        setupMultiChoiceSpinner(locationSpinner, selectedLocations, "Select Locations");
        setupMultiChoiceSpinner(staffSpinner, selectedStaffs, "Select Staff");

        // Check if columnGroups is not null before using it
        if (columnGroups != null) {
            organizeAndDisplayData(lineardisplay, columnGroups);
        } else {
            // Handle the case when columnGroups is null (data not ready yet)
            Log.d("Debug", "Column groups are null. Data may not be ready yet.");
        }

        // Apply button
        ImageButton applyButton = rootView.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyFilters();
            }
        });
        return rootView;
    }

    /**
     * Helper method to set up a Spinner with dynamic options for multi-selection.
     *
     * @param spinner       The Spinner to set up for multi-selection.
     * @param selectedItems Boolean array representing selected items in the Spinner.
     * @param title         The title of the multi-choice dialog.
     */
    private void setupMultiChoiceSpinner(Spinner spinner, boolean[] selectedItems, String title) {
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Show multi-choice dialog when spinner is touched
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    showMultiChoiceDialog(spinner, selectedItems, title);
                }
                return true;
            }
        });
    }

    /**
     * Helper method to show multi-choice dialog for a Spinner.
     *
     * @param spinner       The Spinner for which the multi-choice dialog is displayed.
     * @param selectedItems Boolean array representing selected items in the Spinner.
     * @param title         The title of the multi-choice dialog.
     */
    private void showMultiChoiceDialog(Spinner spinner, boolean[] selectedItems, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(title);

        builder.setMultiChoiceItems(getSpinnerItems(spinner), selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Update the selectedItems array based on user selection
                selectedItems[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the spinner text based on selected items
                updateSpinnerText(spinner, selectedItems);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }

    /**
     * Helper method to get the items from a spinner.
     *
     * @param spinner The Spinner from which to retrieve items.
     * @return Array of CharSequence representing items in the Spinner.
     */
    private CharSequence[] getSpinnerItems(Spinner spinner) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int count = adapter.getCount();
        CharSequence[] items = new CharSequence[count];
        for (int i = 0; i < count; i++) {
            items[i] = adapter.getItem(i).toString();
        }
        return items;
    }

    /**
     * Helper method to update the spinner text based on selected items.
     *
     * @param spinner       The Spinner to update.
     * @param selectedItems Boolean array representing selected items in the Spinner.
     */
    private void updateSpinnerText(Spinner spinner, boolean[] selectedItems) {
        StringBuilder selectedText = new StringBuilder();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                if (selectedText.length() > 0) {
                    selectedText.append(", ");
                }
                selectedText.append(spinner.getItemAtPosition(i).toString());
            }
        }
        // Update the spinner text
        ((TextView) spinner.getSelectedView()).setText(selectedText.toString());
    }

    /**
     * Helper method to set up a Spinner with dynamic options.
     *
     * @param spinner The Spinner to set up.
     * @param options List of String representing options for the Spinner.
     * @param hint    The hint to be added as the default selection.
     */
    private void setupSpinner(Spinner spinner, List<String> options, String hint) {
        // Add a hint to the options
        options.add(0, "Select "+ hint);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set the hint as the default selection
        spinner.setSelection(0);
    }

    /**
     * Helper method to extract unique options from a specific column in columnGroups.
     *
     * @param columnIndex The index of the column to extract unique options from.
     * @return List of String representing unique options in the specified column.
     */
    private List<String> extractUniqueOptions(int columnIndex) {
        List<String> options = new ArrayList<>();

        for (List<String> group : columnGroups) {
            if (columnIndex >= 0 && columnIndex < group.size()) {
                String option = group.get(columnIndex);
                option = removeLabel(option);
                if (!options.contains(option)) {
                    options.add(option);
                }
            }
        }

        return options;
    }

    /**
     * Custom comparator to sort days in the order of Monday to Friday.
     */
    private static class DayComparator implements Comparator<String> {
        private final String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        @Override
        public int compare(String day1, String day2) {
            int index1 = getIndex(day1);
            int index2 = getIndex(day2);
            return Integer.compare(index1, index2);
        }

        private int getIndex(String day) {
            for (int i = 0; i < daysOfWeek.length; i++) {
                if (daysOfWeek[i].equalsIgnoreCase(day)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     * Organizes and displays the course data in the specified LinearLayout based on days and times.
     *
     * @param displayColumnsLayout The LinearLayout to display the organized data.
     * @param data                 List of column groups containing course information.
     */
    private void organizeAndDisplayData(LinearLayout displayColumnsLayout, List<List<String>> data) {
        // Use TreeMap with a custom comparator to sort days in the desired order (Monday to Friday)
        Map<String, Map<String, List<List<String>>>> dayTimeData = new TreeMap<>(new DayComparator());

        // Organize data
        for (List<String> group : data) {
            String day = getDayFromGroup(group);
            String time = getTimeFromGroup(group);

            if (!dayTimeData.containsKey(day)) {
                dayTimeData.put(day, new TreeMap<>()); // TreeMap for sorting times
            }

            if (!dayTimeData.get(day).containsKey(time)) {
                dayTimeData.get(day).put(time, new ArrayList<>());
            }

            dayTimeData.get(day).get(time).add(group);
        }

        // Display organized data
        for (Map.Entry<String, Map<String, List<List<String>>>> dayEntry : dayTimeData.entrySet()) {
            // Display day
            TextView dayTextView = new TextView(requireContext());
            dayTextView.setText(dayEntry.getKey());
            displayColumnsLayout.addView(dayTextView);

            // Display time and corresponding data
            for (Map.Entry<String, List<List<String>>> timeEntry : dayEntry.getValue().entrySet()) {
                TextView timeTextView = new TextView(requireContext());
                timeTextView.setText("Time: " + timeEntry.getKey());
                displayColumnsLayout.addView(timeTextView);

                for (List<String> group : timeEntry.getValue()) {
                    // Format and display each group
                    String formattedGroup = formatGroup(group);
                    TextView groupTextView = new TextView(requireContext());
                    groupTextView.setText(formattedGroup);
                    displayColumnsLayout.addView(groupTextView);
                }
            }
        }
    }

    /**
     * Helper method to get the day information from a group.
     *
     * @param group List of String representing a group of course information.
     * @return String representing the day information.
     */
    private String getDayFromGroup(List<String> group) {
        // Assuming that Column 5 contains the day information
        return group.get(4).split(":")[1].trim();
    }

    /**
     * Helper method to get the start time information from a group.
     *
     * @param group List of String representing a group of course information.
     * @return String representing the start time information.
     */
    private String getTimeFromGroup(List<String> group) {
        // Assuming that Column 6 contains the start time information
        return group.get(5).split(":")[1].trim();
    }

    /**
     * Helper method to get the duration information from a group.
     *
     * @param group List of String representing a group of course information.
     * @return String representing the duration information.
     */
    private String getDurationFromGroup(List<String> group) {
        // Assuming that Column 8 contains the duration information
        return group.get(7).split(":")[1].trim().replaceAll("Column 8: ", "");
    }

    /**
     * Helper method to format a group of course information for display.
     *
     * @param group List of String representing a group of course information.
     * @return String representing the formatted group for display.
     */
    private String formatGroup(List<String> group) {
        StringBuilder formattedGroup = new StringBuilder();

        // Extract relevant information and format the group
        formattedGroup.append("Duration: ").append(getDurationFromGroup(group)).append(" hour(s)\n");
        formattedGroup.append("Module: ").append(removeLabel(group.get(1))).append("\n");
        formattedGroup.append("Type: ").append(removeLabel(group.get(2))).append("\n");
        formattedGroup.append("Location: ").append(removeLabel(group.get(9))).append("\n");
        formattedGroup.append("Staff: ").append(removeLabel(group.get(11))).append("\n\n");

        return formattedGroup.toString();
    }

    /**
     * Helper method to remove "Column x: " labels from an element.
     *
     * @param element The element from which to remove the label.
     * @return String representing the element without the label.
     */
    private String removeLabel(String element) {
        // Remove "Column x: " labels from the element
        return element.replaceAll("Column \\d+: \\s*", "");
    }

    /**
     * Applies the selected filters (module, type, location, staff) to the course data
     * and displays the filtered data.
     */
    private void applyFilters() {
        // Get selected options from the Spinners
        String selectedModule = getSelectedItems(moduleSpinner, selectedModules);
        String selectedType = getSelectedItems(typeSpinner, selectedTypes);
        String selectedLocation = getSelectedItems(locationSpinner, selectedLocations);
        String selectedStaff = getSelectedItems(staffSpinner, selectedStaffs);

        // Filter the data based on selected options
        List<List<String>> filteredData = filterData(selectedModule, selectedType, selectedLocation, selectedStaff);

        // Display the filtered data
        displayFilteredData(filteredData);
    }

    /**
     * Helper method to get selected items from a Spinner as a comma-separated string.
     *
     * @param spinner       The Spinner from which to get selected items.
     * @param selectedItems Boolean array representing selected items in the Spinner.
     * @return String representing selected items as a comma-separated string.
     */
    private String getSelectedItems(Spinner spinner, boolean[] selectedItems) {
        StringBuilder selectedText = new StringBuilder();
        for (int i = 0; i < selectedItems.length; i++) {
            if (selectedItems[i]) {
                if (selectedText.length() > 0) {
                    selectedText.append(", ");
                }
                selectedText.append(spinner.getItemAtPosition(i).toString());
            }
        }
        return selectedText.toString();
    }


    /**
     * Displays the filtered course data based on selected module, type, location, and staff.
     *
     * @param filteredData List of column groups containing filtered course information.
     */
    private void displayFilteredData(List<List<String>> filteredData) {
        // Clear existing views from the layout
        lineardisplay.removeAllViews();

        if (filteredData.isEmpty()) {
            // Display a message if there is no data after filtering
            TextView noDataTextView = new TextView(requireContext());
            noDataTextView.setText("No data matching the selected filters.");
            lineardisplay.addView(noDataTextView);
        } else {
            // Display the filtered data
            organizeAndDisplayData(lineardisplay, filteredData);
        }
    }

    /**
     * Filters the original course data based on selected module, type, location, and staff.
     *
     * @param selectedModule   Comma-separated string of selected modules.
     * @param selectedType     Comma-separated string of selected types.
     * @param selectedLocation Comma-separated string of selected locations.
     * @param selectedStaff    Comma-separated string of selected staff members.
     * @return List of column groups containing filtered course information.
     */
    private List<List<String>> filterData(String selectedModule, String selectedType, String selectedLocation, String selectedStaff) {
        List<List<String>> filteredData = new ArrayList<>();

        // Loop through the original data (columnGroups) and include only the groups that match the selected options
        for (List<String> group : columnGroups) {
            // Extract relevant information for filtering
            String module = removeLabel(group.get(1));
            String type = removeLabel(group.get(2));
            String location = removeLabel(group.get(9));
            String staff = removeLabel(group.get(11));

            // Check if the group matches the selected options
            boolean moduleMatch = selectedModule.isEmpty() || selectedModule.contains(module);
            boolean typeMatch = selectedType.isEmpty() || selectedType.contains(type);
            boolean locationMatch = selectedLocation.isEmpty() || selectedLocation.contains(location);
            boolean staffMatch = selectedStaff.isEmpty() || selectedStaff.contains(staff);

            // If all conditions are true, include the group in the filtered data
            if (moduleMatch && typeMatch && locationMatch && staffMatch) {
                filteredData.add(group);
            }
        }

        return filteredData;
    }


}