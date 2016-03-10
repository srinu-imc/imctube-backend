package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.db.utils.Util;
import com.imctube.cinema.model.Movie;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import static java.util.Arrays.asList;

import jersey.repackaged.com.google.common.collect.Sets;

public class MovieDb {

    public static List<Movie> getMoviesClipified() {
        return getMovies(new BasicDBObject("clipified", true));
    }

    public static List<Movie> getMoviesToClipify() {
        return getMovies(new BasicDBObject("$or", asList(new BasicDBObject("clipified", false),
                new BasicDBObject("clipified", new BasicDBObject("$exists", false)))));
    }

    public static List<Movie> getMoviesToReview() {
        return getMovies(
                new BasicDBObject("$or",
                        asList(new BasicDBObject("reviewed", false),
                                new BasicDBObject("reviewed", new BasicDBObject("$exists", false)))).append("clipified",
                                        true));
    }

    public static List<Movie> getMoviesReviewed() {
        return getMovies(new BasicDBObject("reviewed", true));
    }

    public static List<Movie> getAllMovies() {
        return getMovies(new BasicDBObject());
    }

    private static List<Movie> getMovies(BasicDBObject queryObject) {
        DBCollection movieCollection = MongoDbClient.getMovieCollection();
        DBCursor cursor = movieCollection.find(queryObject);

        List<Movie> movieList = new ArrayList<Movie>();
        while (cursor.hasNext()) {
            movieList.add(JsonToJavaConverter.parseMovie(cursor.next().toString()));
        }
        return movieList;
    }

    public static List<Movie> getMovies(String artistId) {
        DBCursor cursor = MongoDbClient.getMovieCollection().find(new BasicDBObject("_id",
                new BasicDBObject("$in", Util.getObjectIds(ArtistDb.getArtist(artistId).getMovieIds()))));

        List<Movie> movieList = new ArrayList<Movie>();
        while (cursor.hasNext()) {
            movieList.add(JsonToJavaConverter.parseMovie(cursor.next().toString()));
        }
        return movieList;
    }

    public static Movie getMovie(String movieId) {
        DBCollection movieCollection = MongoDbClient.getMovieCollection();

        return JsonToJavaConverter
                .parseMovie(movieCollection.findOne(new BasicDBObject("_id", new ObjectId(movieId))).toString());
    }

    public static Movie addMovie(Movie movie) {
        DBCollection movieCollection = MongoDbClient.getMovieCollection();

        ObjectId movieId = new ObjectId();
        movie.setId(movieId.toString());

        DBObject jsonObject = JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(movie));
        jsonObject.put("_id", movieId);

        movieCollection.insert(jsonObject);
        return movie;
    }

    public static Movie removeMovie(String movieId) {
        DBCollection movieCollection = MongoDbClient.getMovieCollection();

        DBObject movie = movieCollection.findAndRemove(new BasicDBObject("_id", new ObjectId(movieId)));
        return JsonToJavaConverter.parseMovie(movie.toString());
    }

    public static Movie updateMovie(Movie movie) {
        DBCollection movieCollection = MongoDbClient.getMovieCollection();
        Movie existingMovie = getMovie(movie.getId());
        movie.setClipIds(Sets.union(movie.getClipIdSet(), existingMovie.getClipIdSet()));

        return JsonToJavaConverter
                .parseMovie(
                        movieCollection
                                .findAndModify(new BasicDBObject("_id", new ObjectId(movie.getId())),
                                        JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(movie)))
                                .toString());
    }
}
