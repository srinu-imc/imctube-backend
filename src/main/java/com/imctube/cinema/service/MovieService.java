package com.imctube.cinema.service;

import java.util.List;

import com.imctube.cinema.db.ArtistDb;
import com.imctube.cinema.db.MovieDb;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Movie;

public class MovieService {

    public MovieService() {
    }

    public List<Movie> getMoviesToClipfy() {
        return MovieDb.getMoviesToClipify();
    }

    public List<Movie> getMoviesClipified() {
        return MovieDb.getMoviesClipified();
    }

    public List<Movie> getMoviesToReview() {
        return MovieDb.getMoviesToReview();
    }

    public List<Movie> getMoviesReviewed() {
        return MovieDb.getMoviesReviewed();
    }

    public List<Movie> getAllMovies() {
        return MovieDb.getAllMovies();
    }

    public List<Movie> getMovies(String artistId) {
        return MovieDb.getMovies(artistId);
    }

    public Movie getMovie(String movieId) {
        return MovieDb.getMovie(movieId);
    }

    public Movie addMovie(Movie movie) {
        return MovieDb.addMovie(movie);
    }

    public Movie updateMovie(String movieId, Movie movie) {
        movie.setId(movieId);
        return MovieDb.updateMovie(movie);
    }

    public Movie removeMovie(String movieId) {
        return MovieDb.removeMovie(movieId);
    }

    public List<Artist> getArtists(String movieId) {
        Movie movie = getMovie(movieId);
        return ArtistDb.getArtists(movie.getArtistIdSet());
    }
}
