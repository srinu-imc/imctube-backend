package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.db.utils.Util;
import com.imctube.cinema.model.Artist;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import jersey.repackaged.com.google.common.collect.Lists;
import jersey.repackaged.com.google.common.collect.Sets;

public class ArtistDb {

    public static List<Artist> getArtists(boolean onlyHaveMovies) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();
        DBCursor cursor = artistCollection.find().sort(new BasicDBObject("industryName", 1));

        List<Artist> artistList = new ArrayList<Artist>();
        while (cursor.hasNext()) {
            Artist artist = JsonToJavaConverter.parseArtist(cursor.next().toString());
            if (onlyHaveMovies && artist.getMovieIds().isEmpty()) {
                continue;
            }
            artistList.add(artist);
        }
        return artistList;
    }

    public static List<Artist> getArtists(Set<String> artistIds) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();

        DBCursor cursor = artistCollection
                .find(new BasicDBObject("_id",
                        new BasicDBObject("$in", Util.getObjectIds(Lists.newArrayList(artistIds)))))
                .sort(new BasicDBObject("industryName", 1));

        List<Artist> artistList = new ArrayList<Artist>();
        while (cursor.hasNext()) {
            artistList.add(JsonToJavaConverter.parseArtist(cursor.next().toString()));
        }
        return artistList;
    }

    public static Artist getArtist(String artistId) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();

        DBObject artist = artistCollection.findOne(new BasicDBObject("_id", new ObjectId(artistId)));
        return JsonToJavaConverter.parseArtist(artist.toString());
    }

    public static Artist addArtist(Artist artist) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();

        ObjectId artistId = new ObjectId();
        artist.setId(artistId.toString());

        DBObject object = JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(artist));
        object.put("_id", artistId);

        artistCollection.insert(object);
        return artist;
    }

    public static Artist removeArtist(String artistId) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();

        DBObject artist = artistCollection.findAndRemove(new BasicDBObject("_id", new ObjectId(artistId)));
        return JsonToJavaConverter.parseArtist(artist.toString());
    }

    public static Artist updateArtist(Artist artist) {
        DBCollection artistCollection = MongoDbClient.getArtistCollection();
        Artist existing = getArtist(artist.getId());

        artist.setMovieIds(Sets.union(existing.getMovieIdSet(), artist.getMovieIdSet()));

        return JsonToJavaConverter
                .parseArtist(
                        artistCollection
                                .findAndModify(new BasicDBObject("_id", new ObjectId(artist.getId())),
                                        JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(artist)))
                                .toString());
    }

    public static Artist addMovie(String artistId, String movieId) {
        Artist artist = getArtist(artistId);
        if (artist.getMovieIdSet().contains(movieId)) {
            return artist;
        } else {
            artist.addMovieId(movieId);
            return updateArtist(artist);
        }
    }
}
