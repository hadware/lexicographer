package com.lexicographer;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * Created by robin on 14/01/16.
 */
public class MongoUtils {

    private static MongoClient mongoClient;

    public static void connect() {
        mongoClient = new MongoClient();
    }

    public static void update(String statName, String id, int value) {
        MongoDatabase db = mongoClient.getDatabase("test");
        db.getCollection("in").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)));
    }

    public static void close() {
        mongoClient.close();
    }
}
