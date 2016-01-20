package com.lexicographer;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;

/**
 * Created by robin on 14/01/16.
 */
public class MongoUtils {

    private static MongoClient mongoClient;
    private static MongoDatabase db;

    public static void connect() {
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("test");
    }

    public static void update(String statName, String id, int value) {
        db.getCollection("in").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)));
    }

    public static void update(String statName, String id, float value) {
        db.getCollection("in").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)));
    }

    public static void addWordGlossary(String docId, String wordStemme, int occurency) {
        db.getCollection("in").updateOne(new Document("_id", new ObjectId(docId)),
                new Document("$addToSet", new Document("glossary",
                        new Document().append("word", wordStemme)
                                .append("occ", occurency)
                                .append("tf", (0.5 + 0.5 * (occurency / getOccMax(docId)))))));
    }

    public static double getOccMax(String docId) {
        final Document first = db.getCollection("in").find(new Document("_id", new ObjectId(docId))).first();
        return first.getDouble("maxOcc");
    }

    public static void close() {
        mongoClient.close();
    }
}
