package com.imctube.cinema.model;

public class Dialogue {

    // Dialogue native language words typed in English
    String text;

    String artistId;

    public Dialogue() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }
}
