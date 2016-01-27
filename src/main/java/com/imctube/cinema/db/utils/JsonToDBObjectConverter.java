package com.imctube.cinema.db.utils;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class JsonToDBObjectConverter {
    public static DBObject convert(String jsonString) {
	return (DBObject) JSON.parse(jsonString);
    }
}
