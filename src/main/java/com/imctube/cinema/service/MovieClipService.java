package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.MovieClipDb;
import com.imctube.cinema.model.MovieClip;

public class MovieClipService {
    public MovieClipService() {
    }

    public List<MovieClip> getMovieClips() {
	return MovieClipDb.getMovieClips();
    }

    public List<MovieClip> getMovieClips(String movieId) {
	return MovieClipDb.getMovieClips(movieId);
    }

    public List<MovieClip> getArtistMovieClips(String artistId) {
	return MovieClipDb.getArtistMovieClips(artistId);
    }

    public List<MovieClip> getMovieClips(String artistId, String movieId) {
	return MovieClipDb.getMovieClips(artistId, movieId);
    }

    public MovieClip getMovieLastAddedClip(String movieId) {
	return MovieClipDb.getMovieLastAddedClip(movieId);	
    }
    
    public MovieClip getMovieClip(String clipId) {
	return MovieClipDb.getMovieClip(clipId);
    }

    public MovieClip tagArtistToMovieClip(String clipId, String artistId) {
	return MovieClipDb.tagArtistToMovieClip(clipId, artistId);
    }

    public MovieClip addMovieClip(String movieId, MovieClip clip) {
	return MovieClipDb.addMovieClip(movieId, clip);
    }

    public MovieClip updateMovieClip(String clipId, MovieClip clip) {
	clip.setClipId(clipId);
	return MovieClipDb.updateMovieClip(clip);
    }

    public MovieClip removeMovieClip(String movieClipId) {
	return MovieClipDb.removeMovieClip(movieClipId);
    }
}
