package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.ClipViewLog;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ClipViewLogDb {

    public static List<ClipViewLog> getClipViewLogs() {
        DBCollection clipViewLogCollection = MongoDbClient.getClipViewLogCollection();
        DBCursor cursor = clipViewLogCollection.find();
        List<ClipViewLog> clipViewLogList = new ArrayList<ClipViewLog>();
        while (cursor.hasNext()) {
            clipViewLogList.add(JsonToJavaConverter.parseClipViewLog(cursor.next().toString()));
        }
        return clipViewLogList;
    }

    public static void insertClipView(String clipId, String host, String userId) {
        DBCollection clipViewLogCollection = MongoDbClient.getClipViewLogCollection();
        clipViewLogCollection.insert(new BasicDBObject("clipId", clipId).append("host", host).append("userId", userId)
                .append("at", new Date()));
    }

    public static void insertClipView(ClipViewLog viewLog) {
        DBCollection clipViewLogCollection = MongoDbClient.getClipViewLogCollection();

        DBObject jsonViewLog = JsonToDBObjectConverter.convert(JavaToJsonConverter.covert(viewLog));
        clipViewLogCollection.insert(jsonViewLog);
    }
}
