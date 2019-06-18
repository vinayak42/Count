package com.example.count.view;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

@Entity
public class Counter implements Serializable {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    private String title;
    private int value;

    @ColumnInfo(name = "createdTimestamp")
    @TypeConverters({TimestampConverter.class})
    private Date creationTimestamp;

    @ColumnInfo(name = "lastUpdationTimestamp")
    @TypeConverters({TimestampConverter.class})
    private Date lastUpdationTimestamp;

    private int goal;

    public Counter() {
        // required for Firestore's automatic data mapping
    }

    public Counter(String id, String title, Date creationTimestamp, Date lastUpdationTimestamp, int goal, int value) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.creationTimestamp = creationTimestamp;
        this.lastUpdationTimestamp = lastUpdationTimestamp;
        this.goal = goal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public void setLastUpdationTimestamp(Date lastUpdationTimestamp) {
        this.lastUpdationTimestamp = lastUpdationTimestamp;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public Date getLastUpdationTimestamp() {
        return lastUpdationTimestamp;
    }

    public int getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", value=" + value +
                ", creationTimestamp=" + creationTimestamp +
                ", lastUpdationTimestamp=" + lastUpdationTimestamp +
                ", goal=" + goal +
                '}';
    }
}
