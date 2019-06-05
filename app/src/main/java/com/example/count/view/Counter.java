package com.example.count.view;

import java.util.Date;

public final class Counter {
    private String id;
    private String counterTitle;
    private long counterValue;
    private Date creationTimestamp;
    private Date lastUpdationTimestamp;
    private int goal;

    public Counter() {
    }

    public Counter(String id, String counterTitle, Date creationTimestamp, Date lastUpdationTimestamp, int goal) {
        this.id = id;
        this.counterTitle = counterTitle;
        this.counterValue = 0;
        this.creationTimestamp = creationTimestamp;
        this.lastUpdationTimestamp = lastUpdationTimestamp;
        this.goal = goal;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCounterTitle(String counterTitle) {
        this.counterTitle = counterTitle;
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

    public long getCounterValue() {
        return counterValue;
    }

    public String getCounterTitle() {
        return counterTitle;
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
