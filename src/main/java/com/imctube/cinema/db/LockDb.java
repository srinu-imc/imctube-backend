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

    private static List<Lock> getLocks(BasicDBObject query) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        DBCursor cursor = lockCollection.find(query);

        List<Lock> lockList = new ArrayList<Lock>();
        while (cursor.hasNext()) {
            lockList.add(getLock(cursor.next()));
        }
        return lockList;
    }

    public static List<Lock> getMovieLocks() {
        return getLocks(new BasicDBObject("movieId", new BasicDBObject("$exists", true)));
    }

    public static List<Lock> getMovieClipLocks() {
        return getLocks(new BasicDBObject("clipId", new BasicDBObject("$exists", true)));
    }

    private static Lock getLock(DBObject object) {
        Lock lock = new Lock();
        if (object.get("movieId") != null) {
            lock.setMovieId((String) object.get("movieId"));
        }
        if (object.get("clipId") != null) {
            lock.setClipId((String) object.get("clipId"));
        }
        if (object.get("userId") != null) {
            lock.setUserId((String) object.get("userId"));
        }
        lock.setStartAt((Date) object.get("startAt"));
        if(object.get("count") != null) {
            lock.setCount((int) object.get("count"));
        }
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

    public static Optional<Lock> getLockByMovieClipId(String clipId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();

        DBObject lock = lockCollection.findOne(new BasicDBObject("clipId", clipId));
        if (lock != null) {
            return Optional.of(getLock(lock));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<Lock> getMovieLockByUserId(String userId) {
        return getLock(new BasicDBObject("userId", userId).append("movieId", new BasicDBObject("$exists", true)));
    }

    public static Optional<Lock> getMovieClipLockByUserId(String userId) {
        return getLock(new BasicDBObject("userId", userId).append("clipId", new BasicDBObject("$exists", true)));
    }

    private static Optional<Lock> getLock(BasicDBObject query) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();

        DBObject lock = lockCollection.findOne(query);
        if (lock != null) {
            return Optional.of(getLock(lock));
        } else {
            return Optional.empty();
        }
    }

    public static Lock addLock(Lock lock) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();

        BasicDBObject dbLock = new BasicDBObject("startAt", lock.getStartAt()).append("userId", lock.getUserId());

        if (lock.getMovieId() != null) {
            dbLock.append("movieId", lock.getMovieId());
        } else if (lock.getClipId() != null) {
            dbLock.append("clipId", lock.getClipId());
        }

        dbLock.append("count", lock.getCount());

        lockCollection.insert(dbLock);
        return lock;
    }

    public static Lock updateLock(Lock lock) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        lock.setStartAt(new Date());

        BasicDBObject dbLock = new BasicDBObject("startAt", lock.getStartAt()).append("userId", lock.getUserId());

        DBObject rlock = null;
        if (lock.getMovieId() != null) {
            dbLock.append("movieId", lock.getMovieId());
            rlock = lockCollection.findAndModify(new BasicDBObject("movieId", lock.getMovieId()), dbLock);
        } else if (lock.getClipId() != null) {
            dbLock.append("clipId", lock.getClipId());
            rlock = lockCollection.findAndModify(new BasicDBObject("clipId", lock.getClipId()), dbLock);
        }

        return getLock(rlock);
    }

    public static Lock removeMovieLockByUserId(String userId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        return getLock(lockCollection.findAndRemove(
                new BasicDBObject("userId", userId).append("movieId", new BasicDBObject("$exists", true))));
    }

    public static Lock removeMovieClipLockByUserId(String userId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        return getLock(lockCollection.findAndRemove(
                new BasicDBObject("userId", userId).append("clipId", new BasicDBObject("$exists", true))));
    }

    public static Lock removeMovieClipLockByClipId(String clipId) {
        DBCollection lockCollection = MongoDbClient.getLockCollection();
        return getLock(lockCollection.findAndRemove(new BasicDBObject("clipId", clipId)));
    }
}
