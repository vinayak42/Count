package com.example.count.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CounterDao {

    @Insert
    public void insert(Counter counter);

    @Query("SELECT * FROM Counter ORDER BY lastUpdationTimestamp DESC")
    LiveData<List<Counter>> getAllCounters();

    @Query("SELECT * FROM Counter ORDER BY lastUpdationTimestamp DESC")
    List<Counter> getAllCountersList();

    @Delete
    public void delete(Counter counter);

    @Update
    public void update(Counter counter);

    @Query("DELETE FROM Counter")
    public void deleteAll();

    @Query("SELECT * FROM Counter WHERE id = :counterId")
    Counter getCounter(String counterId);
}
