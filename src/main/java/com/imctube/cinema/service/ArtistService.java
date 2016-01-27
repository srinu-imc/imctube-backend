package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.ArtistDb;
import com.imctube.cinema.model.Artist;

public class ArtistService {
    public ArtistService() {
    }

    public List<Artist> getArtists() {
	return ArtistDb.getArtists();
    }

    public Artist getArtist(String artistId) {
	return ArtistDb.getArtist(artistId);
    }

    public Artist addArtist(Artist artist) {
	return ArtistDb.addArtist(artist);
    }

    public Artist updateArtist(String artistId, Artist artist) {
	artist.setId(artistId);
	return ArtistDb.updateArtist(artist);
    }

    public Artist removeArtist(String artistId) {
	return ArtistDb.removeArtist(artistId);
    }
}
