package com.example.nottonline.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Room Database class representing the local database for storing Event entities.
 * Extends {@link androidx.room.RoomDatabase}.
 */
@Database(entities = {Event.class}, version = 1)
public abstract class EventDatabase extends RoomDatabase {

    /**
     * Abstract method to get the DAO (Data Access Object) interface for Event entities.
     *
     * @return The EventDao interface for performing database operations on Event entities.
     */
    public abstract EventDao getEventDao();
}

