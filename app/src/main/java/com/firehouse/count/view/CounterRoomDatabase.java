package com.firehouse.count.view;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

@Database(entities = {Counter.class}, version = 1)
public abstract class CounterRoomDatabase extends RoomDatabase {

    // TODO implement data migration strategy before production:
    // Refer this: https://medium.com/google-developers/understanding-migrations-with-room-f01e04b07929

    private static CounterRoomDatabase instance;

//    private static Callback roomDatabaseCallback = new Callback() {
//        // Reference: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#11
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            new PopulateDbAsync(instance).execute();
//        }
//    };

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

    /*
    This class is useless. All it will do is whenever the database is opened, it will delete all of it
    and reload the data from the arraylist passed to it. This is dumb.
    PS, however, just in order to call the method in it, add .addCallback to the above build() sequence.
     */
    private static class PopulateDbAsync extends AsyncTask<ArrayList<Counter>, Void, Void> {

        private final CounterDao counterDao;

        public PopulateDbAsync(CounterRoomDatabase db) {
            counterDao = db.counterDao();
        }

        @Override
        protected Void doInBackground(ArrayList<Counter>... arrayLists) {
            ArrayList<Counter> counterArrayList = arrayLists[0];
            counterDao.deleteAll();
            for (Counter counter: counterArrayList) {
                counterDao.insert(counter);
            }
            return null;
        }
    }
}
