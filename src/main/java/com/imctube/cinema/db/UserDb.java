package com.imctube.cinema.db;

import java.util.Optional;

import org.bson.types.ObjectId;

import com.imctube.cinema.db.utils.JavaToJsonConverter;
import com.imctube.cinema.db.utils.JsonToDBObjectConverter;
import com.imctube.cinema.db.utils.JsonToJavaConverter;
import com.imctube.cinema.model.User;
import com.imctube.cinema.model.User.Provider;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class UserDb {
    public static Optional<User> getUserByProviderId(Provider provider, String userId) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        switch (provider) {
        case FACEBOOK:
            DBObject user = userCollection.findOne(new BasicDBObject("facebook", userId));
            if (user != null) {
                return Optional.of(JsonToJavaConverter.parseUser(user.toString()));
            } else {
                return Optional.empty();
            }
        case GOOGLE:
            user = userCollection.findOne(new BasicDBObject("google", userId));
            if (user != null) {
                return Optional.of(JsonToJavaConverter.parseUser(user.toString()));
            } else {
                return Optional.empty();
            }
        default:
            return Optional.empty();
        }
    }

    public static Optional<User> getUserByEmail(String email) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        DBObject user = userCollection.findOne(new BasicDBObject("email", email));
        if (user != null) {
            return Optional.of(JsonToJavaConverter.parseUser(user.toString()));
        } else {
            return Optional.empty();
        }
    }

    public static Optional<User> getUserById(String userId) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        DBObject user = userCollection.findOne(new BasicDBObject("_id", new ObjectId(userId)));
        if (user != null) {
            return Optional.of(JsonToJavaConverter.parseUser(user.toString()));
        } else {
            return Optional.empty();
        }
    }

    public static User addUser(User user) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        ObjectId userId = new ObjectId();
        user.setId(userId.toString());

        DBObject jsonObject = JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(user));
        jsonObject.put("_id", userId);

        userCollection.insert(jsonObject);
        return user;
    }

    public static User removeUser(String userId) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        DBObject user = userCollection.findAndRemove(new BasicDBObject("_id", new ObjectId(userId)));
        return JsonToJavaConverter.parseUser(user.toString());
    }

    public static User updateUser(User user) {
        DBCollection userCollection = MongoDbClient.getUserCollection();

        return JsonToJavaConverter
                .parseUser(userCollection.findAndModify(new BasicDBObject("_id", new ObjectId(user.getId())),
                        JsonToDBObjectConverter.convert(JavaToJsonConverter.convert(user))).toString());
    }
}
