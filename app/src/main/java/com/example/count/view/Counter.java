package com.example.count.view;

import java.io.Serializable;
import java.util.Date;

public final class Counter implements Serializable {
    private String id;
    private String title;
    private int value;
    private Date creationTimestamp;
    private Date lastUpdationTimestamp;
    private int goal;

    public Counter() {
        // required for Firestore's automatic data mapping
    }

    public Counter(String id, String title, Date creationTimestamp, Date lastUpdationTimestamp, int goal) {
        this.id = id;
        this.title = title;
        this.value = 0;
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
}
