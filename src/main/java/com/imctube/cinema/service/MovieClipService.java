package com.imctube.cinema.service;

import java.util.List;
import java.util.Optional;

import com.imctube.cinema.db.ArtistDb;
import com.imctube.cinema.db.MovieClipDb;
import com.imctube.cinema.model.Artist;
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

    public List<MovieClip> getMovieClipsToReview(String movieId) {
        return MovieClipDb.getMovieClipsToReview(movieId);
    }

    public List<MovieClip> getMovieClipsReviewed(String movieId) {
        return MovieClipDb.getMovieClipsReviewed(movieId);
    }

    public List<MovieClip> getArtistMovieClips(String artistId) {
        return MovieClipDb.getArtistMovieClips(artistId);
    }

    public List<MovieClip> getMovieClips(String artistId, String movieId) {
        return MovieClipDb.getMovieClips(artistId, movieId);
    }

    public MovieClip getMovieLastAddedClip(String movieId) {
        Optional<MovieClip> movieClip = MovieClipDb.getMovieLastAddedClip(movieId);
        if (movieClip.isPresent()) {
            return movieClip.get();
        } else {
            return null;
        }
    }

    public MovieClip getMovieClip(String clipId) {
        return MovieClipDb.getMovieClip(clipId);
    }

    public Optional<MovieClip> getPrevMovieClip(String clipId) {
        MovieClip current = getMovieClip(clipId);
        return MovieClipDb.getMovieClipByEndTimeAndMovieId(current.getMovieId(), current.getStartTime());
    }

    public Optional<MovieClip> getNextMovieClip(String clipId) {
        MovieClip current = getMovieClip(clipId);
        return MovieClipDb.getMovieClipByStartTimeAndMovieId(current.getMovieId(), current.getEndTime());
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

    public List<Artist> getArtists(String clipId) {
        MovieClip clip = getMovieClip(clipId);
        return ArtistDb.getArtists(clip.getArtistIds());
    }
}
