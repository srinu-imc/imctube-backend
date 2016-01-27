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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
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
