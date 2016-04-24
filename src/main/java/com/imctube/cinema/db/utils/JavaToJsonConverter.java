package com.imctube.cinema.db.utils;

import com.google.gson.Gson;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.ClipViewLog;
import com.imctube.cinema.model.ErrorMessage;
import com.imctube.cinema.model.Lock;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.model.TinyUrl;
import com.imctube.cinema.model.Token;
import com.imctube.cinema.model.User;

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

    public static String convert(User user) {
        return gson.toJson(user);
    }

    public static String convert(Token token) {
        return gson.toJson(token);
    }

	public static String convert(TinyUrl token) {
        return gson.toJson(token);
    }

    public static String convert(ErrorMessage message) {
        return gson.toJson(message);
    }

    public static String covert(Lock lock) {
        return gson.toJson(lock);
    }

    public static String covert(ClipViewLog clipViewLog) {
        return gson.toJson(clipViewLog);
    }
}
