package com.example.count.view;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Counter.class}, version = 1)
public abstract class CounterRoomDatabase extends RoomDatabase {

    // TODO implement data migration strategy before production:
    // Refer this: https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929

    private static CounterRoomDatabase instance;

    public static CounterRoomDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (CounterRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            CounterRoomDatabase.class, "counter_database")
                            .build();
                }
            }
        }

        return instance;
    }

    public abstract CounterDao counterDao();
}
