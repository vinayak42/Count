package com.example.count.view;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;

@Dao
public interface CounterDao {

    @Insert
    public void insert(Counter counter);

    @Query("SELECT * FROM Counter ORDER BY lastUpdationTimestamp")
    ArrayList<Counter> getAllCounters();

    @Delete
    public void delete(Counter counter);

    @Update
    public void update(Counter counter);
}
