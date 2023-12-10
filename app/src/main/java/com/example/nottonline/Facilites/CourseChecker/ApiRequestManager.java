package com.example.nottonline.Facilites.CourseChecker;

import android.content.Context;

import com.koushikdutta.ion.Ion;

/**
 * The {@code ApiRequestManager} class is responsible for handling API requests using
 * the Ion library. It provides a convenient way to perform GET requests and receive
 * callbacks for success or failure.
 */
public class ApiRequestManager {
    /**
     * The context in which the API requests are made.
     */
    private Context context;

    /**
     * Constructs an {@code ApiRequestManager} with the given context.
     *
     * @param context The context in which API requests are made.
     */
    public ApiRequestManager(Context context) {
        this.context = context;
    }

    /**
     * Performs a GET request to the specified URL and provides callbacks for success
     * or failure.
     *
     * @param urlString The URL for the GET request.
     * @param callback  The callback to handle the result or error.
     */
    public void performGetRequest(String urlString, Callback callback) {
        Ion.with(context)
                .load(urlString)
                .asString()
                .setCallback((e, result) -> {
                    if (e == null) {
                        callback.onSuccess(result);
                    } else {
                        callback.onError(e.getMessage());
                    }
                });
    }

    /**
     * The callback interface for handling success and error responses from API requests.
     */
    public interface Callback {
        /**
         * Called when the API request is successful.
         *
         * @param result The result of the API request.
         */
        void onSuccess(String result);

        /**
         * Called when there is an error in the API request.
         *
         * @param error The error message describing the issue.
         */
        void onError(String error);
    }
}
