package com.imctube.cinema.model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import jersey.repackaged.com.google.common.collect.Lists;

@XmlRootElement
public class Artist {
    // Artist unique Id populated by backend when artist is added to the cinema
    // database
    private String id;

    // Artist industry name
    private String industryName;

    // Artist first name
    private String firstName;

    // Artist last name
    private String lastName;

    // Artist nick names
    private Set<String> nickNames = new HashSet<String>();

    // Artist date of birth
    private Date dateOfBirth;

    // Artist thumbnail
    private String thumbnail;

    // Movies in which artist appeared, populated when artist is tagged in movie
    private Set<String> movieIds = new HashSet<String>();

    // UserId of the person who added this artist
    private String addedBy;

    // UserIds of people who modified this artist
    private Set<String> modifiedBy = new HashSet<String>();

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

    public String getFirstName() {
        return firstName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<String> getNickNames() {
        return nickNames;
    }

    public void setNickNames(Set<String> nickNames) {
        this.nickNames = nickNames;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<String> getMovieIds() {
        return Lists.newArrayList(movieIds);
    }

    public Set<String> getMovieIdSet() {
        return movieIds;
    }

    public void setMovieIds(Set<String> movieIds) {
        this.movieIds = movieIds;
    }

    public void addMovieId(String movieId) {
        movieIds.add(movieId);
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
