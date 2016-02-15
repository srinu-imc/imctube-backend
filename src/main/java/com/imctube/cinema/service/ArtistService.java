package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.ArtistDb;
import com.imctube.cinema.model.Artist;

public class ArtistService {
    public ArtistService() {
    }

    public List<Artist> getArtists(boolean onlyHaveMovies) {
        return ArtistDb.getArtists(onlyHaveMovies);
    }

    public Artist getArtist(String artistId) {
        return ArtistDb.getArtist(artistId);
    }

    public Artist addArtist(Artist artist) {
        return ArtistDb.addArtist(artist);
    }

    public Artist addMovie(String artistId, String movieId) {
        return ArtistDb.addMovie(artistId, movieId);
    }

    public Artist updateArtist(String artistId, Artist artist) {
        artist.setId(artistId);
        return ArtistDb.updateArtist(artist);
    }

    public Artist removeArtist(String artistId) {
        return ArtistDb.removeArtist(artistId);
    }
}
