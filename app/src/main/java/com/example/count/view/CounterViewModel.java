package com.example.count.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.count.model.CounterSyncWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CounterViewModel extends AndroidViewModel {

    private CounterRepository counterRepository;
    private LiveData<List<Counter>> allCounters;
    private WorkManager workManager;
    private PeriodicWorkRequest periodicSyncWorkRequest;

    public CounterViewModel(@NonNull Application application) {
        super(application);
        counterRepository = new CounterRepository(application);
        allCounters = counterRepository.getAllCounters();
        workManager = WorkManager.getInstance();
        periodicSyncWorkRequest = new PeriodicWorkRequest.Builder(CounterSyncWorker.class, 15, TimeUnit.MINUTES).build();
        workManager.enqueue(periodicSyncWorkRequest);
    }

    public LiveData<List<Counter>> getAllCounters() {
        return allCounters;
    }

    public void insert(Counter counter) {
        counterRepository.insert(counter);
    }

    public void update(Counter counter) {
        counterRepository.update(counter);
    }

    public void delete(Counter counter) {
        counterRepository.delete(counter);
    }
}
