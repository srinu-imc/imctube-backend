package com.imctube.cinema.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Lock {

    Date startAt;

    String movieId;

    String userId;

    public Lock() {
    }

    public Lock(String movieId, String userId) {
        this.movieId = movieId;
        this.userId = userId;
        this.startAt = new Date();
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

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
