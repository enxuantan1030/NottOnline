package com.example.nottonline.Facilites.CourseChecker;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.nottonline.R;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment that allows users to check course schedules by selecting a school, course, year, week, and day.
 * It performs API requests to retrieve and display the course schedule data.
 *
 *
 */
public class CourseCheckerFragment extends Fragment {

    private Spinner schoolDropdown;
    private Spinner courseDropdown;
    private Spinner yearDropdown;
    private Spinner weekdropdown;
    private Spinner daydropdown;
    private HashMap<String, List<String>> schoolToCoursesMap;
    private HtmlParser htmlParser;
    private ApiRequestManager apiRequestManager;
    private List<List<String>> columns;

    private static final String COURSE_CHECKER_TAG = "CourseCheckerFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course_checker, container, false);

        ImageButton button = rootView.findViewById(R.id.checkButton);
        button.setOnClickListener(v -> {
            String selectedSchool = schoolDropdown.getSelectedItem().toString();
            List<String> courses = schoolToCoursesMap.get(selectedSchool);

            constructURL(courses);
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiRequestManager = new ApiRequestManager(requireContext());

        schoolDropdown = view.findViewById(R.id.schooldropdown);
        courseDropdown = view.findViewById(R.id.coursedropdown);
        yearDropdown = view.findViewById(R.id.yeardropdown);
        weekdropdown = view.findViewById(R.id.weekdropdown);
        daydropdown = view.findViewById(R.id.daydropdown);

        initializeSchoolToCoursesMap();

        ArrayAdapter<CharSequence> schoolAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.schooloptions, android.R.layout.simple_spinner_item);
        schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolDropdown.setAdapter(schoolAdapter);

        schoolDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateCourseDropdown();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    /**
     * initializes the mapping of schools to their corresponding courses.
     * Retrieves course arrays from resources and adds mappings to the map.
     */
    private void initializeSchoolToCoursesMap() {
        // Retrieve the arrays from resources
        String[] appMathCourses = getResources().getStringArray(R.array.appMath_courses);
        String[] appPsychMgmtOptions = getResources().getStringArray(R.array.appPsychMgmtOptions);
        String[] foundationArtsEdOptions = getResources().getStringArray(R.array.foundationArtsEdOptions);
        String[] biomedicalSciOptions = getResources().getStringArray(R.array.biomedicalSciOptions);
        String[] biotechnologyOptions = getResources().getStringArray(R.array.biotechnologyOptions);
        String[] foundationBusMgmtOptions = getResources().getStringArray(R.array.foundationBusMgmtOptions);
        String[] chemicalEnvEngOptions = getResources().getStringArray(R.array.chemicalEnvEngOptions);
        String[] civilEngOptions = getResources().getStringArray(R.array.civilEngOptions);
        String[] compSciAIShortOptions = getResources().getStringArray(R.array.compSciAIShortOptions);
        String[] eeEngOptions = getResources().getStringArray(R.array.eeEngOptions);
        String[] economicsOptions = getResources().getStringArray(R.array.economicsOptions);
        String[] educationOptions = getResources().getStringArray(R.array.educationOptions);
        String[] engineeringFoundationOptions = getResources().getStringArray(R.array.engineeringFoundationOptions);
        String[] englishCreativeWritingOptions = getResources().getStringArray(R.array.englishCreativeWritingOptions);

        // Initialize the member variable
        schoolToCoursesMap = new HashMap<>();

        // Add mappings for each school
        schoolToCoursesMap.put("App Math", Arrays.asList(appMathCourses));
        schoolToCoursesMap.put("App Psych", Arrays.asList(appPsychMgmtOptions));
        schoolToCoursesMap.put("Art & Ed F", Arrays.asList(foundationArtsEdOptions));
        schoolToCoursesMap.put("Biom Sci", Arrays.asList(biomedicalSciOptions));
        schoolToCoursesMap.put("Biosci", Arrays.asList(biotechnologyOptions));
        schoolToCoursesMap.put("Bus & M F", Arrays.asList(foundationBusMgmtOptions));
        schoolToCoursesMap.put("Chem & EE", Arrays.asList(chemicalEnvEngOptions));
        schoolToCoursesMap.put("Civ Eng", Arrays.asList(civilEngOptions));
        schoolToCoursesMap.put("Comp Sci", Arrays.asList(compSciAIShortOptions));
        schoolToCoursesMap.put("E & EE", Arrays.asList(eeEngOptions));
        schoolToCoursesMap.put("Economics", Arrays.asList(economicsOptions));
        schoolToCoursesMap.put("Education", Arrays.asList(educationOptions));
        schoolToCoursesMap.put("Eng (Fn)", Arrays.asList(engineeringFoundationOptions));
        schoolToCoursesMap.put("English", Arrays.asList(englishCreativeWritingOptions));
    }

    /**
     * updates the course dropdown based on the selected school.
     * Retrieves the list of courses for the selected school and updates the dropdown.
     */
    private void updateCourseDropdown() {
        String selectedSchool = schoolDropdown.getSelectedItem().toString();
        List<String> courses = schoolToCoursesMap.get(selectedSchool);

        if (courses != null) {
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(
                    requireContext(), android.R.layout.simple_spinner_item, courses);
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseDropdown.setAdapter(courseAdapter);

            Log.d("Debug", "Selected School: " + selectedSchool);
            Log.d("Debug", "Available Courses: " + courses.toString());
        } else {
            Log.d("Debug", "No courses available for the selected school: " + selectedSchool);
        }
    }

    /**
     * Opens the DisplayCourseCheckerFragment with filtered column groups.
     *
     * @param columnGroups List of column groups to be filtered and displayed.
     */
    private void openDisplayCourseFragment(List<List<String>> columnGroups) {
        List<List<String>> filteredGroups = new ArrayList<>();
        for (List<String> group : columnGroups) {
            if (group.size() == 12) {
                filteredGroups.add(group);
            }
        }

        if (!filteredGroups.isEmpty()) {
            Log.d("Debug", "Number of groups passed to DisplayCourseCheckerFragment: " + filteredGroups.size());

            // Pass data to DisplayCourseCheckerFragment
            DisplayCourseCheckerFragment displayFragment = new DisplayCourseCheckerFragment(filteredGroups);

            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, displayFragment, "DisplayCourseCheckerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            showAlertDialog("Data Not Found", "There's no data in this course", COURSE_CHECKER_TAG);
        }
    }

    /**
     * Constructs the URL for API request based on selected dropdown values.
     *
     * @param courses List of courses associated with the selected school.
     */
    private void constructURL(List<String> courses) {
        if (courseDropdown.getSelectedItem() != null) {
            String selectedCourseValue = getSelectedCourseValue();
            String selectedYearValue = getSelectedValue(yearDropdown, R.array.yearoptions);
            String selectedWeekValue = getSelectedValue(weekdropdown, R.array.weeksOptions);
            String selectedDayValue = getSelectedValue(daydropdown, R.array.dayoptions);

            Log.d("Debug", "Selected Course: " + selectedCourseValue);
            Log.d("Debug", "Selected Year: " + selectedYearValue);
            Log.d("Debug", "Selected Week: " + selectedWeekValue);
            Log.d("Debug", "Selected Day: " + selectedDayValue);

            String url = "http://timetablingunmc.nottingham.ac.uk:8006/reporting/TextSpreadsheet;programme+of+study;id;"
                    + selectedCourseValue + selectedYearValue + "%0D%0A?days=" + selectedDayValue + "&weeks=" + selectedWeekValue
                    + "&periods=3-20&template=SWSCUST+programme+of+study+TextSpreadsheet&height=100&week=100";

            Log.d("Debug", "Constructed URL: " + url);

            apiRequestManager.performGetRequest(url, new ApiRequestManager.Callback() {
                @Override
                public void onSuccess(String result) {
                    columns = new HtmlParser().extractColumns(result);
                    if (columns != null) {
                        // Process the columns as needed
                        openDisplayCourseFragment(columns);
                    } else {
                        showAlertDialog("Data Not Found", "Columns are null. Data may not be ready yet.", null);
                    }
                }

                @Override
                public void onError(String error) {
                    showAlertDialog("Error", "API request failed: " + error, null);
                }
            });
        } else {
            showAlertDialog("Error", "No course selected!", null);
        }
    }

    /**
     * Gets the selected value of the course dropdown.
     *
     * @return Selected course value.
     */
    private String getSelectedCourseValue() {
        int selectedPosition = courseDropdown.getSelectedItemPosition();
        List<String> courses = schoolToCoursesMap.get(schoolDropdown.getSelectedItem().toString());
        String[] courseOptions = courses != null ? courses.toArray(new String[0]) : new String[0];
        String[] selectedCourseParts = courseOptions[selectedPosition].split(",");
        return selectedCourseParts.length > 0 ? selectedCourseParts[0] : "";
    }

    /**
     * Gets the selected value from the given spinner and array resource.
     *
     * @param spinner  Spinner widget.
     * @param arrayId  Resource ID of the string array associated with the spinner.
     * @return Selected value.
     */
    private String getSelectedValue(Spinner spinner, int arrayId) {
        int selectedPosition = spinner.getSelectedItemPosition();
        String[] options = getResources().getStringArray(arrayId);
        String[] selectedParts = options[selectedPosition].split(",");
        return selectedParts.length > 0 ? selectedParts[0] : "";
    }

    /**
     * Displays an alert dialog with the provided title and message.
     *
     * @param title        Title of the alert dialog.
     * @param message      Message to be displayed in the alert dialog.
     * @param fragmentTag  Tag of the fragment associated with the alert dialog (can be null).
     */
    private void showAlertDialog(String title, String message, @Nullable String fragmentTag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Handle OK button click if needed
                    if (fragmentTag != null) {
                        goBackToCourseCheckerFragment(fragmentTag);
                    }
                });

        builder.show();
    }

    /**
     * Navigates back to the CourseCheckerFragment using the fragment tag.
     *
     * @param fragmentTag Tag associated with the CourseCheckerFragment.
     */
    private void goBackToCourseCheckerFragment(String fragmentTag) {
        Fragment courseCheckerFragment = getParentFragmentManager().findFragmentByTag(fragmentTag);

        if (courseCheckerFragment != null) {
            getParentFragmentManager().popBackStackImmediate(fragmentTag, 0);
        } else {
            // Handle the case when CourseCheckerFragment is not found
            Log.d("Debug", "CourseCheckerFragment not found in the back stack.");
        }
    }


}
