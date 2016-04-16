package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.ClipViewLast2Min;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ClipViewLast2MinDb {

    public static List<ClipViewLast2Min> getClipViewLast2Mins() {
        DBCollection clipViewLast2MinCollection = MongoDbClient.getClipViewLast2MinCollection();
        DBCursor cursor = clipViewLast2MinCollection.find();
        List<ClipViewLast2Min> clipViewLast2MinList = new ArrayList<ClipViewLast2Min>();
        while (cursor.hasNext()) {
            clipViewLast2MinList.add(JsonToJavaConverter.parseClipViewLast2Min(cursor.next().toString()));
        }
        return clipViewLast2MinList;
    }

    public static boolean isClipViewedInLast2Min(String clipId, String host) {
        DBCollection clipViewLast2MinCollection = MongoDbClient.getClipViewLast2MinCollection();
        DBObject clipViewLast2Min = clipViewLast2MinCollection
                .findOne(new BasicDBObject("clipId", clipId).append("host", host));
        if (clipViewLast2Min != null) {
            return true;
        } else {
            return false;
        }

    }

    public static void insertClipView(String clipId, String host) {
        DBCollection clipViewLast2MinCollection = MongoDbClient.getClipViewLast2MinCollection();
        clipViewLast2MinCollection
                .insert(new BasicDBObject("clipId", clipId).append("host", host).append("at", new Date()));
    }
}
