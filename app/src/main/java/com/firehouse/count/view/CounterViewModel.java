package com.firehouse.count.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public final class CounterViewModel extends AndroidViewModel {

    private CounterRepository counterRepository;
    private LiveData<List<Counter>> allCounters;

    public CounterViewModel(@NonNull Application application) {
        super(application);
        counterRepository = new CounterRepository(application);
        allCounters = counterRepository.getAllCounters();
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
