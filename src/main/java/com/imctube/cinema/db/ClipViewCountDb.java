package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.ClipViewCount;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class ClipViewCountDb {

    public static List<ClipViewCount> getClipViewCounts() {
        DBCollection clipViewCountCollection = MongoDbClient.getClipViewCountCollection();
        DBCursor cursor = clipViewCountCollection.find().sort(new BasicDBObject("count", -1));
        List<ClipViewCount> clipViewCountList = new ArrayList<ClipViewCount>();
        while (cursor.hasNext()) {
            clipViewCountList.add(JsonToJavaConverter.parseClipViewCount(cursor.next().toString()));
        }
        return clipViewCountList;
    }

    public static List<ClipViewCount> getClipViewCounts(List<String> clipIds) {
        DBCollection clipViewCountCollection = MongoDbClient.getClipViewCountCollection();
        DBCursor cursor = clipViewCountCollection.find(new BasicDBObject("clipId", new BasicDBObject("$in", clipIds)))
                .sort(new BasicDBObject("count", -1));
        List<ClipViewCount> clipViewCountList = new ArrayList<ClipViewCount>();
        while (cursor.hasNext()) {
            clipViewCountList.add(JsonToJavaConverter.parseClipViewCount(cursor.next().toString()));
        }
        return clipViewCountList;
    }

    public static Optional<ClipViewCount> getClipViewCount(String clipId) {
        DBCollection clipViewCountCollection = MongoDbClient.getClipViewCountCollection();

        DBObject clipViewCount = clipViewCountCollection.findOne(new BasicDBObject("clipId", clipId));
        if (clipViewCount != null) {
            return Optional.of(JsonToJavaConverter.parseClipViewCount(clipViewCount.toString()));
        } else {
            return Optional.empty();
        }
    }

    public static void incrClipViewCount(String clipId) {
        DBCollection clipViewCountCollection = MongoDbClient.getClipViewCountCollection();
        WriteResult result = clipViewCountCollection.update(new BasicDBObject("clipId", clipId),
                new BasicDBObject("$inc", new BasicDBObject("count", 1)));
        if (result.getN() == 0) {
            clipViewCountCollection.insert(new BasicDBObject("clipId", clipId).append("count", 1));
        }
    }
}
