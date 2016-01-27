package com.imctube.cinema.db;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDbClient {
    private static MongoClient mongo = null;
    private static Logger logger = Logger.getLogger(MongoDbClient.class);

    private MongoDbClient() {

    }

    private static synchronized MongoClient getMongoInstance() {
        if (mongo == null) {
            try {
                mongo = new MongoClient("localhost", 27017);
            } catch (UnknownHostException e) {
                logger.error("Failed to connect to mongo server");
            }
        }
        return mongo;
    }

    private static DB getCinemaDB() {
        return getMongoInstance().getDB("cinema");
    }

    public static DBCollection getArtistCollection() {
        return getCinemaDB().getCollection("artists");
    }

    public static DBCollection getMovieCollection() {
        return getCinemaDB().getCollection("movies");
    }

    public static DBCollection getClipCollection() {
        return getCinemaDB().getCollection("clips");
    }
}
