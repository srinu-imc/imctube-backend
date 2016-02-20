package com.imctube.cinema.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.imctube.cinema.model.Lock;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class LockDb {

    public static List<Lock> getLocks() {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        DBCursor cursor = lockCollection.find();

        List<Lock> lockList = new ArrayList<Lock>();
        while (cursor.hasNext()) {
            lockList.add(getLock(cursor.next()));
        }
        return lockList;
    }

    private static Lock getLock(DBObject object) {
        Lock lock = new Lock();
        lock.setMovieId((String) object.get("movieId"));
        lock.setUserId((String) object.get("userId"));
        lock.setStartAt((Date) object.get("startAt"));

        return lock;
    }

    public static Optional<Lock> getLockByMovieId(String movieId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();

        DBObject lock = lockCollection.findOne(new BasicDBObject("movieId", movieId));
        if (lock != null) {
            return Optional.of(getLock(lock));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Lock> getLockByUserId(String userId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();

        DBObject lock = lockCollection.findOne(new BasicDBObject("userId", userId));
        if (lock != null) {
            return Optional.of(getLock(lock));
        } else {
            return Optional.empty();
        }
    }

    public static Lock addLock(Lock lock) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        DBObject dbLock = new BasicDBObject("startAt", lock.getStartAt()).append("movieId", lock.getMovieId())
                .append("userId", lock.getUserId());

        lockCollection.insert(dbLock);
        return lock;
    }

    public static Lock updateLock(Lock lock) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        lock.setStartAt(new Date());

        DBObject dbLock = new BasicDBObject("startAt", lock.getStartAt()).append("movieId", lock.getMovieId())
                .append("userId", lock.getUserId());

        return getLock(lockCollection.findAndModify(new BasicDBObject("movieId", lock.getMovieId()), dbLock));
    }

    public static Lock removeLockByUserId(String userId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        return getLock(lockCollection.findAndRemove(new BasicDBObject("userId", userId)));
    }
}
