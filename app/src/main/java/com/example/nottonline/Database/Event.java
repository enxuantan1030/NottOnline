package com.example.nottonline.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * Entity class representing an event in the application.
 * Events can be stored in the local database.
 */
@Entity(tableName = "Event")
public class Event implements Serializable {

    /**
     * The auto-generated unique identifier for the event.
     */
    @ColumnInfo(name = "Event_ID")
    @PrimaryKey(autoGenerate = true)
    int id;

    /**
     * The title of the event.
     */
    @ColumnInfo(name = "Title")
    String Title;

    /**
     * The description of the event.
     */
    @ColumnInfo(name = "Description")
    String Description;

    /**
     * The date of the event.
     */
    @ColumnInfo(name = "Date")
    String date;

    /**
     * The time of the event.
     */
    @ColumnInfo(name = "Time")
    String time;

    /**
     * The user ID associated with the event.
     */
    @ColumnInfo(name = "User_ID")
    int User_ID;

    /**
     * Indicates whether the event has a notification.
     */
    @ColumnInfo(name = "Notification")
    boolean Notification;

    /**
     * Indicates whether the event has an alarm.
     */
    @ColumnInfo(name = "Alarm")
    boolean Alarm;

    /**
     * The notification message associated with the event.
     */
    @ColumnInfo(name = "NotificationMessage")
    String NotificationMessage;

    /**
     * Default constructor for Room database. Used for ignoring during database operations.
     */
    @Ignore
    public Event() {

    }

    /**
     * Constructor to create an Event with specified details.
     *
     * @param Title             The title of the event.
     * @param Description       The description of the event.
     * @param date              The date of the event.
     * @param time              The time of the event.
     * @param User_ID           The user ID associated with the event.
     * @param Alarm             Indicates whether the event has an alarm.
     * @param Notification      Indicates whether the event has a notification.
     * @param NotificationMessage The notification message associated with the event.
     */
    public Event(String Title, String Description, String date, String time, int User_ID, boolean Alarm, boolean Notification,String NotificationMessage){
        this.Title = Title;
        this.Description = Description;
        this.date = date;
        this.time = time;
        this.User_ID = User_ID;
        this.Notification = Notification;
        this.Alarm = Alarm;
        this.NotificationMessage = NotificationMessage;
        this.id = 0;
    }

    /**
     * Getter for the title of the event.
     *
     * @return The title of the event.
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Setter for the title of the event.
     *
     * @param title The title of the event.
     */
    public void setTitle(String title) {
        Title = title;
    }

    /**
     * Getter for the description of the event.
     *
     * @return The description of the event.
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Setter for the description of the event.
     *
     * @param description The description of the event.
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Getter for the date of the event.
     *
     * @return The date of the event.
     */
    public String getDate() {
        return date;
    }

    /**
     * Setter for the date of the event.
     *
     * @param date The date of the event.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Getter for the time of the event.
     *
     * @return The time of the event.
     */
    public String getTime() {
        return time;
    }

    /**
     * Setter for the time of the event.
     *
     * @param time The time of the event.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Getter for whether the event has a notification.
     *
     * @return True if the event has a notification, false otherwise.
     */
    public boolean getNotification() {
        return Notification;
    }

    /**
     * Setter for whether the event has a notification.
     *
     * @param notification True if the event has a notification, false otherwise.
     */
    public void setNotification(boolean notification) {
        this.Notification = notification;
    }

    /**
     * Getter for whether the event has an alarm.
     *
     * @return True if the event has an alarm, false otherwise.
     */
    public boolean getAlarm() {
        return Alarm;
    }

    /**
     * Setter for whether the event has an alarm.
     *
     * @param alarm True if the event has an alarm, false otherwise.
     */
    public void setAlarm(boolean alarm) {
        this.Alarm = alarm;
    }

    /**
     * Getter for the user ID associated with the event.
     *
     * @return The user ID associated with the event.
     */
    public int getUser_ID() {
        return User_ID;
    }

    /**
     * Setter for the user ID associated with the event.
     *
     * @param user_ID The user ID associated with the event.
     */
    public void setUser_ID(int user_ID) {
        this.User_ID = user_ID;
    }

    /**
     * Getter for the unique identifier of the event.
     *
     * @return The unique identifier of the event.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the unique identifier of the event.
     *
     * @param eventId The unique identifier of the event.
     */
    public void setId(int eventId) {
        this.id = eventId;
    }

    /**
     * Getter for the notification message associated with the event.
     *
     * @return The notification message associated with the event.
     */
    public String getNotificationMessage() {
        return NotificationMessage;
    }

    /**
     * Setter for the notification message associated with the event.
     *
     * @param notificationMessage The notification message associated with the event.
     */
    public void setNotificationMessage(String notificationMessage) {
        this.NotificationMessage = notificationMessage;
    }
}
