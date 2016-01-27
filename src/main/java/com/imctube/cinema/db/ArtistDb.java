package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.Artist;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ArtistDb {

    public static List<Artist> getArtists() {
	DBCollection artistCollection = MongoDbClient.getArtistCollection();
	DBCursor cursor = artistCollection.find();

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

	return JsonToJavaConverter
		.parseArtist(
			artistCollection
				.findAndModify(new BasicDBObject("_id", new ObjectId(artist.getId())),
					JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(artist)))
				.toString());
    }
}
