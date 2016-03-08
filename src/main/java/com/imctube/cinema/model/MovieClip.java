package com.imctube.cinema.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MovieClip {
    // Clip unique ID generated by backend when movie clip is added to database
    String clipId;

    // Movie Id
    String movieId;

    // Video Id
    String videoId;

    // Movie name
    String movieName;

    // start time of the clip
    double startTime;

    // end time of the clip
    double endTime;

    // Description of the clip
    String description;

    // List of artists, populated when artist is tagged in movie clip
    Set<String> artistIds = new HashSet<String>();

    Set<String> thumbnails = new HashSet<String>();
    // List of dialogues in movie clip, populated when dialogue is added to
    // movieClip
    List<Dialogue> dialogues = new ArrayList<Dialogue>();

    String clipifiedBy;

    Set<String> reviewedBy = new HashSet<String>();

    boolean reviewed = false;

    public MovieClip() {

    }

    public String getClipifiedBy() {
        return clipifiedBy;
    }

    public void setClipifiedBy(String clipifiedBy) {
        this.clipifiedBy = clipifiedBy;
    }

    public Set<String> getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Set<String> reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public void addReviewedBy(String userId) {
        this.reviewedBy.add(userId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Set<String> getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(Set<String> thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getClipId() {
        return clipId;
    }

    public void setClipId(String clipId) {
        this.clipId = clipId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(Set<String> artistIds) {
        this.artistIds = artistIds;
    }

    public void addArtistId(String artist) {
        this.artistIds.add(artist);
    }

    public List<Dialogue> getDialogues() {
        return dialogues;
    }

    public void setDialogues(List<Dialogue> dialogues) {
        this.dialogues = dialogues;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
