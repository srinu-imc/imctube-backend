package com.imctube.cinema.db.utils;

import com.google.gson.Gson;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;

public class JsonToJavaConverter {

    public static Gson gson = new Gson();

    public static Artist parseArtist(String jsonArtist) {
	return gson.fromJson(jsonArtist, Artist.class);
    }

    public static Movie parseMovie(String jsonMovie) {
	return gson.fromJson(jsonMovie, Movie.class);
    }

    public static MovieClip parseMovieClip(String jsonClip) {
	return gson.fromJson(jsonClip, MovieClip.class);
    }
}
