package com.example.count.view;

import com.google.firebase.Timestamp;

public final class Counter {
    private String counterId;
    private String counterTitle;
    private long counterValue;
    private Timestamp creationTime;
    private Timestamp lastUpdatedTime;
    private int goal;

    public Counter() {
    }

    public Counter(String counterId, String counterTitle, Timestamp creationTime, Timestamp lastUpdatedTime, int goal) {
        this.counterId = counterId;
        this.counterTitle = counterTitle;
        this.counterValue = 0;
        this.creationTime = creationTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.goal = goal;
    }

    public void setCounterId(String counterId) {
        this.counterId = counterId;
    }

    public void setCounterTitle(String counterTitle) {
        this.counterTitle = counterTitle;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public void setLastUpdatedTime(Timestamp lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public String getCounterId() {
        return counterId;
    }

    public long getCounterValue() {
        return counterValue;
    }

    public String getCounterTitle() {
        return counterTitle;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public Timestamp getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public int getGoal() {
        return goal;
    }
}
