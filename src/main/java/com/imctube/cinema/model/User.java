package com.imctube.cinema.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    private String id;

    private String email;

    private String password;

    private String displayName;

    private String facebook;

    private String google;

    public User() {
    }

    public User(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGoogle() {
        return google;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public enum Provider {
        FACEBOOK("facebook"), GOOGLE("google");

        String name;

        Provider(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public String capitalize() {
            return this.name().toUpperCase();
        }
    }

    public void setProviderId(final Provider provider, final String value) {
        switch (provider) {
        case FACEBOOK:
            this.facebook = value;
            break;
        case GOOGLE:
            this.google = value;
            break;
        default:
            throw new IllegalArgumentException();
        }
    }
}
