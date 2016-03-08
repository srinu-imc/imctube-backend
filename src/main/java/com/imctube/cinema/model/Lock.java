package com.imctube.cinema.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lock {

    Date startAt;

    String movieId;

    String clipId;

    String userId;

    Type lockType;

    int count;

    public Lock() {
    }

    public Lock(String userId) {
        this.userId = userId;
        this.startAt = new Date();
        this.lockType = Type.HARD_LOCK;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public String getMovieId() {
        return movieId;
    }

    public Lock setMovieId(String movieId) {
        this.movieId = movieId;
        return this;
    }

    public String getClipId() {
        return clipId;
    }

    public Lock setClipId(String clipId) {
        this.clipId = clipId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Type getLockType() {
        return lockType;
    }

    public void setLockType(Type lockType) {
        this.lockType = lockType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increaseLockCount() {
        this.count++;
    }

    public void decreaseLockCount() {
        this.count--;
    }

    public enum Type {
        SOFT_LOCK, HARD_LOCK
    };
}
