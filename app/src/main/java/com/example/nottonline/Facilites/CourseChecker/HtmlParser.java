package com.example.nottonline.Facilites.CourseChecker;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code HtmlParser} class parses HTML content and extracts specific columns from tables.
 * It uses the Jsoup library for HTML parsing.
 */
public class HtmlParser {

    /**
     * Extracts columns from HTML content and organizes them into groups.
     *
     * @param htmlContent The HTML content to parse.
     * @return A list of groups, where each group represents a set of columns.
     */
    public List<List<String>> extractColumns(String htmlContent) {
        Document document = Jsoup.parse(htmlContent);

        // Find the table with the desired columns
        Elements tables = document.select("table");

        List<List<String>> columnGroups = new ArrayList<>();
        List<String> currentGroup = new ArrayList<>();

        for (Element table : tables) {
            // Find all rows in the table
            Elements rows = table.select("tr");

            // Iterate through rows and extract data
            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements columns = row.select("td");

                // Adjust the loop limit based on the actual number of columns
                int loopLimit = Math.min(columns.size(), 12); // Use 12 as a maximum limit

                // Extract and check desired columns (change loop limit accordingly)
                List<String> currentColumns = new ArrayList<>();
                for (int j = 0; j < loopLimit; j++) {
                    String data = columns.get(j).text().trim(); // Trim to remove leading and trailing whitespaces
                    currentColumns.add("Column " + (j + 1) + ": " + data);
                }

                // Check if the current columns match the previous group
                if (i > 1 && currentColumns.equals(currentGroup)) {
                    // Add to the current group
                    currentGroup.addAll(currentColumns);
                } else {
                    // Start a new group
                    if (!currentGroup.isEmpty()) {
                        columnGroups.add(new ArrayList<>(currentGroup));
                    }
                    currentGroup = new ArrayList<>(currentColumns);
                }
            }
        }

        // Add the last group
        if (!currentGroup.isEmpty()) {
            columnGroups.add(new ArrayList<>(currentGroup));
        }

        return columnGroups; // Return the extracted columns
    }
}
