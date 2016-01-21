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
        mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("epub");
    }

    public static void updateStat(String statName, String id, int value) {
        db.getCollection("books").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)));
    }

    public static void updateStat(String statName, String id, float value) {
        db.getCollection("books").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)));
    }

    public static void addWordGlossary(String docId, String wordStemme, int occurency) {
        db.getCollection("books").updateOne(new Document("_id", new ObjectId(docId)),
                new Document("$addToSet", new Document("glossary",
                        new Document().append("word", wordStemme)
                                .append("occ", occurency)
                                /*.append("tf", (0.5 + 0.5 * (occurency / getOccMax(docId))))*/)));
        System.out.println("Doc updated " + docId);
    }

    public static double getOccMax(String docId) {
        //final Document first = db.getCollection("books").find(new Document("_id", new ObjectId(docId))).first();
        System.out.println("1");
        //TODO : activé la ligne commentée
        return 1.0;
        //return first.getDouble("maxOcc");
    }

    public static String getInputURI(String collection){
        return String.format("mongodb://localhost/%s", collection);
    }

    public static void close() {
        mongoClient.close();
    }
}
