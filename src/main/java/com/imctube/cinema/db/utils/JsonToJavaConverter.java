package com.imctube.cinema.db.utils;

import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imctube.cinema.model.Artist;
import com.imctube.cinema.model.ErrorMessage;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.imctube.cinema.model.TinyUrl;
import com.imctube.cinema.model.Token;
import com.imctube.cinema.model.User;

public class JsonToJavaConverter {

    public static Gson gson = new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();;

    public static Artist parseArtist(String jsonArtist) {
        return gson.fromJson(jsonArtist, Artist.class);
    }

    public static Movie parseMovie(String jsonMovie) {
        return gson.fromJson(jsonMovie, Movie.class);
    }

    public static MovieClip parseMovieClip(String jsonClip) {
        return gson.fromJson(jsonClip, MovieClip.class);
    }

    public static Token parseToken(String token) {
        return gson.fromJson(token, Token.class);
    }

    public static User parseUser(String user) {
        return gson.fromJson(user, User.class);
    }

    public static TinyUrl parseTinyurl(String tinyUrlObject) {
        return gson.fromJson(tinyUrlObject, TinyUrl.class);
    }

    public static ErrorMessage parseErrorMessage(String message) {
        return gson.fromJson(message, ErrorMessage.class);
    }
}
