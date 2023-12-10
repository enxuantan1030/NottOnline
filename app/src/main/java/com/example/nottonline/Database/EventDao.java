package com.example.nottonline.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) interface for handling Event entities in the local database.
 * Contains methods for performing CRUD operations and querying events.
 */
@Dao
public interface EventDao {

    /**
     * Inserts a new event into the database.
     *
     * @param event The event to be added.
     */
    @Insert
    void addEvent(Event event);

    /**
     * Updates an existing event in the database.
     *
     * @param event The event to be updated.
     */
    @Update
    void updateEvent(Event event);

    /**
     * Deletes an event from the database.
     *
     * @param event The event to be deleted.
     */
    @Delete
    void deleteEvent(Event event);

    /**
     * Retrieves a list of all events in the database.
     *
     * @return List of all events.
     */
    @Query("select * from Event")
    List<Event> getAllEvent();

    /**
     * Retrieves a specific event based on its unique identifier.
     *
     * @param event_id The unique identifier of the event.
     * @return The event with the specified ID.
     */
    @Query("select * from Event where Event_ID == :event_id")
    Event getEvent(int event_id);

    /**
     * Retrieves a list of events for a specific date and user ID, sorted by time.
     *
     * @param date    The date for which events are retrieved.
     * @param user_id The user ID associated with the events.
     * @return List of events for the specified date and user.
     */
    @Query("SELECT * FROM Event WHERE date == :date AND User_ID == :user_id ORDER BY time")
    List<Event> getEventsForDate(String date, int user_id);

    /**
     * Retrieves a specific event based on its unique identifier.
     *
     * @param id The unique identifier of the event.
     * @return The event with the specified ID.
     */
    @Query("SELECT * FROM Event WHERE Event_ID = :id")
    Event getEventById(int id);

    /**
     * Retrieves the maximum (latest) event ID in the database.
     *
     * @return The maximum event ID.
     */
    @Query("SELECT max(Event_ID) FROM Event")
    int getLastInsertedEventId();

    /**
     * Retrieves the top 3 nearest events based on the current date and time.
     *
     * @param currentDate The current date.
     * @param currentTime The current time.
     * @return List of the top 3 nearest events.
     */
    @Query("SELECT * FROM Event WHERE ((date = :currentDate AND time >= :currentTime) OR (date > :currentDate) )AND User_ID == :user_id ORDER BY date DESC, time DESC LIMIT 3")
    List<Event> getTop3NearestEvents(String currentDate, String currentTime, int user_id);

    /**
     * Retrieves the history of notification events based on the current date and time.
     *
     * @param currentDate The current date.
     * @param currentTime The current time.
     * @return List of notification events in history.
     */
    @Query("SELECT * FROM Event WHERE ((date = :currentDate AND time < :currentTime) OR (date < :currentDate) )AND notification = 1 AND User_ID == :user_id ORDER BY date DESC, time DESC")
    List<Event> notificationHistory(String currentDate, String currentTime, int user_id);
}
