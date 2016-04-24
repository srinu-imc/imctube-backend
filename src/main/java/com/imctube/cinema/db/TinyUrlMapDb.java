package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.TinyUrl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class TinyUrlMapDb {

	public static String getFullUrl(String hashUrl) {
		DBCollection tinyUrlCollection = MongoDbClient.getTinyUrlCollection();
		DBObject foundObject = tinyUrlCollection.findOne(new BasicDBObject("tinyUrl", hashUrl));

		if (!hashUrl.isEmpty() && (foundObject == null)) {
			return "Url not Not found";
		}
		TinyUrl fullurl_one = JsonToJavaConverter.parseTinyurl(foundObject.toString());
		return fullurl_one.getFullUrl();
	}

	public static String getTinyUrl(String fullUrl) {
		DBCollection tinyUrlCollection = MongoDbClient.getTinyUrlCollection();
		DBObject foundObject = tinyUrlCollection.findOne(new BasicDBObject("fullUrl", fullUrl));
		if (foundObject == null) {
			if (!fullUrl.isEmpty()) {
				String hashString = new String(fullUrl.hashCode() + "");
				TinyUrl newMap = new TinyUrl(hashString, fullUrl);

				DBObject jsonObject = JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(newMap));
				tinyUrlCollection.insert(jsonObject);

				return hashString;
			} else
				return null;
		} else {
			TinyUrl tinyurl_one = JsonToJavaConverter.parseTinyurl(foundObject.toString());
			return tinyurl_one.getTinyUrl();
		}
	}
}
