package com.lexicographer;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.*;

/**
 * Created by robin on 14/01/16.
 */
public class MongoUtils {

    private static final String ipAddress = "40.115.36.216";
    private static MongoClient mongoClient;
    private static MongoDatabase db;

    public static void connect() {
        mongoClient = new MongoClient(ipAddress, 27017);
        db = mongoClient.getDatabase("epub");
    }

    public static void updateStat(String statName, String id, int value) {
        db.getCollection("bookStats").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)), new UpdateOptions().upsert(true));
    }

    public static void updateStat(String statName, String id, float value) {
        db.getCollection("bookStats").updateOne(new Document("_id", new ObjectId(id)),
                new Document("$set", new Document("stats." + statName, value)), new UpdateOptions().upsert(true));
    }

    public static void addWordsGlossary(String filename) throws FileNotFoundException {
        HashMap<String, ArrayList<DBObject>> map = extractInformation(filename);
        if (map != null) {
            for (Map.Entry<String, ArrayList<DBObject>> entry : map.entrySet()) {
                addWordsMongo(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addWordsMongo(String docId, ArrayList<DBObject> documents) {
        db.getCollection("glossaries").insertOne(new Document("_id", new ObjectId(docId)).append("glossary", documents));
        System.out.println("Doc inserted " + docId);
    }

    public static String getInputURI(String collection) {
        return String.format("mongodb://%s/%s", ipAddress, collection);
    }

    private static HashMap<String, ArrayList<DBObject>> extractInformation(String filename) throws FileNotFoundException {
        File file = new File(filename);
        String docId;
        HashMap<String, ArrayList<DBObject>> map = new HashMap<>();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        BSONDecoder decoder = new BasicBSONDecoder();
        int count = 0;
        try {
            while (inputStream.available() > 0) {
                BSONObject obj = decoder.readObject(inputStream);
                if (obj == null) {
                    break;
                }
                docId = ((String) obj.get("_id")).split("-")[0];
                String word = ((String) obj.get("_id")).split("-")[1];
                Integer occurency = (Integer) obj.get("value");
                DBObject document = new BasicDBObject();
                document.put("word", word);
                document.put("occ", occurency);
                if (!map.containsKey(docId)) {
                    map.put(docId, new ArrayList<DBObject>());
                }
                map.get(docId).add(document);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                return map;
            } catch (IOException e) {
                return null;
            }
        }
    }

    public static void close() {
        mongoClient.close();
    }

    public static void storeIdf(String word, Set<String> ids) {
        DBObject o = new BasicDBObject(word, ids);
        db.getCollection("idf").updateOne(new Document("_id", "idf"),
                new Document("$set", o));
    }

    public static void initIdf() {
        try {
            db.getCollection("idf").drop();
            db.getCollection("idf").insertOne(new Document("_id", "idf"));
        } catch (MongoWriteException e) {
        }
    }
}
