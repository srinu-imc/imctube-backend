package com.imctube.cinema.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlRootElement
public class Movie {
    // Movie uniqueid, populated by backend when movie is added to database
    private String id;

    // Movie title
    private String name;

    // Movie youtube videoId
    private String videoId;

    // Movie Caption
    private String caption;

    // Date of release
    private Date dateOfRelease;

    // Certificate
    private String certificate;

    // Banner
    private String banner;

    // Total thumbnails for the movie
    private int thumbnailCount;

    private boolean clipified = false;

    private boolean reviewed = false;

    // ClipIds populated by backend when movie clip is being added to movie.
    private Set<String> clipIds = new HashSet<String>();

    private Set<String> artistIds = new HashSet<String>();

    // User who helped in adding this movie
    private String addedBy;

    // Set of userIds who helped reviewed this movie
    private Set<String> modifiedBy = new HashSet<String>();

    public Movie() {
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Set<String> getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Set<String> modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void addModifiedBy(String userId) {
        this.modifiedBy.add(userId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(Date dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public List<String> getClipIds() {
        return Lists.newArrayList(clipIds);
    }

    public Set<String> getClipIdSet() {
        return clipIds;
    }

    public void addClipId(String clipId) {
        this.clipIds.add(clipId);
    }

    public void setClipIds(Set<String> clipIds) {
        this.clipIds = clipIds;
    }

    public int getThumbnailCount() {
        return thumbnailCount;
    }

    public void setThumbnailCount(int thumbnailCount) {
        this.thumbnailCount = thumbnailCount;
    }

    public boolean isClipified() {
        return clipified;
    }

    public void setClipified(boolean clipified) {
        this.clipified = clipified;
    }

    public Set<String> getArtistIdSet() {
        return artistIds;
    }

    public List<String> getArtistIds() {
        return Lists.newArrayList(artistIds);
    }

    public void setArtistIds(Set<String> artistIds) {
        this.artistIds = artistIds;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }
}
