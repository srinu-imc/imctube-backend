package com.imctube.cinema.db.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

public class Util {
    public static List<ObjectId> getObjectIds(List<String> ids) {
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (String id : ids) {
            objectIds.add(new ObjectId(id));
        }

        return objectIds;
    }
}
