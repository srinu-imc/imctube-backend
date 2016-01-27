package com.imctube.cinema.db.utils;

import com.google.gson.Gson;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;

public class JavaToJsonConverter {
    public static Gson gson = new Gson();

    public static String convert(Artist artist) {
        return gson.toJson(artist);
    }

    public static String convert(Movie movie) {
        return gson.toJson(movie);
    }

    public static String convert(MovieClip clip) {
        return gson.toJson(clip);
    }
}
