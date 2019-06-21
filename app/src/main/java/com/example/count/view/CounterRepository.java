package com.example.count.view;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public final class CounterRepository {

    // Reference: https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#7

    private CounterDao counterDao;
    private LiveData<List<Counter>> allCounters;

    public CounterRepository(Application application) {
        CounterRoomDatabase rdb = CounterRoomDatabase.getInstance(application);
        counterDao = rdb.counterDao();
        allCounters = counterDao.getAllCounters();
    }

    public LiveData<List<Counter>> getAllCounters() {
        return allCounters;
    }

    public void insert(Counter counter) {
        new insertAsyncTask(counterDao).execute(counter);
    }

    public void delete(Counter counter) {
        new deleteAsyncTask(counterDao).execute(counter);
    }

    public void update(Counter counter) {
        new updateAsyncTask(counterDao).execute(counter);
    }

    public void deleteAllCounters() { new deleteAllCountersAsyncTask(counterDao).execute(); }

    public Counter getCounter(String counterID) {
        try {
            return new getCounterAsyncTask(counterDao).execute(counterID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Counter> getAllCountersList() {
        try {
            return new getAllCountersListAsyncTask(counterDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Counter, Void, Void> {
        private CounterDao asyncTaskDao;

        insertAsyncTask(CounterDao asyncTaskDao) {
            this.asyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Counter... counters) {
            asyncTaskDao.insert(counters[0]);
            return null;
        }
    }

    private class deleteAsyncTask extends AsyncTask<Counter, Void, Void> {
        private CounterDao asyncTaskDao;

        public deleteAsyncTask(CounterDao counterDao) {
            this.asyncTaskDao = counterDao;
        }


        @Override
        protected Void doInBackground(Counter... counters) {
            asyncTaskDao.delete(counters[0]);
            return null;
        }
    }

    private class updateAsyncTask extends AsyncTask<Counter, Void, Void>{

        private CounterDao asyncTaskDao;

        public updateAsyncTask(CounterDao counterDao) {
            this.asyncTaskDao = counterDao;
        }


        @Override
        protected Void doInBackground(Counter... counters) {
            asyncTaskDao.update(counters[0]);
            return null;
        }
    }

    private class deleteAllCountersAsyncTask extends AsyncTask<Void, Void, Void>{

        private CounterDao asyncTaskDao;

        public deleteAllCountersAsyncTask(CounterDao asyncTaskDao) {
            this.asyncTaskDao = asyncTaskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private class getAllCountersListAsyncTask extends AsyncTask<Void, Void, List<Counter>>{

        private CounterDao asyncTaskDao;

        public getAllCountersListAsyncTask(CounterDao counterDao) {
            this.asyncTaskDao = counterDao;
        }


        @Override
        protected List<Counter> doInBackground(Void... voids) {
            return asyncTaskDao.getAllCountersList();
        }
    }

    private class getCounterAsyncTask extends AsyncTask<String, Void, Counter>{

        private CounterDao asyncTaskDao;

        public getCounterAsyncTask(CounterDao counterDao) {
            this.asyncTaskDao = counterDao;
        }

        @Override
        protected Counter doInBackground(String... strings) {

            String counterID = strings[0];

            return asyncTaskDao.getCounter(counterID);
        }
    }
}
