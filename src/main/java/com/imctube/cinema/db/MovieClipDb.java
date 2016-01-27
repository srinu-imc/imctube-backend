package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.db.utils.Util;
import com.imctube.cinema.model.Movie;
import com.imctube.cinema.model.MovieClip;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MovieClipDb {

    public static List<MovieClip> getMovieClips() {
	DBCollection clipCollection = MongoDbClient.getClipCollection();
	DBCursor cursor = clipCollection.find();

	List<MovieClip> clips = new ArrayList<MovieClip>();
	while (cursor.hasNext()) {
	    clips.add(JsonToJavaConverter.parseMovieClip(cursor.next().toString()));
	}
	return clips;
    }

    public static List<MovieClip> getMovieClips(String movieId) {
	DBObject query = new BasicDBObject("_id",
		new BasicDBObject("$in", Util.getObjectIds(MovieDb.getMovie(movieId).getClipIds())));

	DBCollection clipCollection = MongoDbClient.getClipCollection();
	DBCursor cursor = clipCollection.find(query);

	List<MovieClip> clips = new ArrayList<MovieClip>();
	while (cursor.hasNext()) {
	    clips.add(JsonToJavaConverter.parseMovieClip(cursor.next().toString()));
	}
	return clips;
    }

    private static List<String> getClipIds(List<Movie> movies) {
	List<String> clipIds = new ArrayList<String>();
	for (Movie movie : movies) {
	    clipIds.addAll(movie.getClipIds());
	}
	return clipIds;
    }

    public static List<MovieClip> getArtistMovieClips(String artistId) {
	List<Movie> movies = MovieDb.getMovies(artistId);

	DBObject query = new BasicDBObject("_id", new BasicDBObject("$in", Util.getObjectIds(getClipIds(movies))))
		.append("artistIds", artistId);

	DBCollection clipCollection = MongoDbClient.getClipCollection();
	DBCursor cursor = clipCollection.find(query);

	List<MovieClip> clips = new ArrayList<MovieClip>();
	while (cursor.hasNext()) {
	    clips.add(JsonToJavaConverter.parseMovieClip(cursor.next().toString()));
	}
	return clips;
    }

    public static List<MovieClip> getMovieClips(String artistId, String movieId) {
	DBObject query = new BasicDBObject("_id",
		new BasicDBObject("$in", Util.getObjectIds(MovieDb.getMovie(movieId).getClipIds()))).append("artistIds",
			artistId);

	DBCollection clipCollection = MongoDbClient.getClipCollection();
	DBCursor cursor = clipCollection.find(query);

	List<MovieClip> clips = new ArrayList<MovieClip>();
	while (cursor.hasNext()) {
	    clips.add(JsonToJavaConverter.parseMovieClip(cursor.next().toString()));
	}
	return clips;
    }

    public static MovieClip getMovieLastAddedClip(String movieId) {
	DBCollection clipCollection = MongoDbClient.getClipCollection();
	DBObject query = new BasicDBObject("_id",
		new BasicDBObject("$in", Util.getObjectIds(MovieDb.getMovie(movieId).getClipIds())));
	
	DBCursor cursor = clipCollection.find(query)
		.sort(new BasicDBObject("endTime", -1)).limit(1);
	
	return JsonToJavaConverter.parseMovieClip(cursor.next().toString());
    }

    public static MovieClip getMovieClip(String clipId) {
	DBCollection clipCollection = MongoDbClient.getClipCollection();

	return JsonToJavaConverter
		.parseMovieClip(clipCollection.findOne(new BasicDBObject("_id", new ObjectId(clipId))).toString());
    }

    public static MovieClip tagArtistToMovieClip(String clipId, String artistId) {
	MovieClip clip = getMovieClip(clipId);
	clip.addArtistId(artistId);
	return updateMovieClip(clip);
    }

    public static MovieClip addMovieClip(String movieId, MovieClip movieClip) {
	DBCollection clipCollection = MongoDbClient.getClipCollection();
	Movie movie = MovieDb.getMovie(movieId);

	ObjectId clipId = new ObjectId();
	movieClip.setClipId(clipId.toString());
	movie.addClipId(clipId.toString());

	MovieDb.updateMovie(movie);

	DBObject jsonClip = JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(movieClip));
	jsonClip.put("_id", clipId);

	clipCollection.insert(jsonClip);
	return movieClip;
    }

    public static MovieClip removeMovieClip(String clipId) {
	DBCollection clipCollection = MongoDbClient.getClipCollection();

	DBObject clip = clipCollection.findAndRemove(new BasicDBObject("_id", new ObjectId(clipId)));
	return JsonToJavaConverter.parseMovieClip(clip.toString());
    }

    public static MovieClip updateMovieClip(MovieClip movieClip) {
	DBCollection clipCollection = MongoDbClient.getClipCollection();

	return JsonToJavaConverter
		.parseMovieClip(
			clipCollection
				.findAndModify(new BasicDBObject("_id", new ObjectId(movieClip.getClipId())),
					JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(movieClip)))
				.toString());
    }
}
