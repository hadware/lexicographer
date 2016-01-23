package com.lexicographer;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static void addWordsGlossary(String filename) throws FileNotFoundException {
        HashMap<String, ArrayList<DBObject>> map = convertToJSONString(filename);
        for(Map.Entry<String, ArrayList<DBObject>> entry : map.entrySet()) {
            addWordsMongo(entry.getKey(), entry.getValue());
        }
    }

    private static void addWordsMongo(String docId, ArrayList<DBObject> documents) {
        db.getCollection("books").updateOne(new Document("_id", new ObjectId(docId)),
                new Document("$addToSet", new Document("glossary", documents)));
        System.out.println("Doc updated " + docId);
    }

    //TODO : Return an HashMap <docId, occ>
    private static HashMap<String, Integer> getOccsMax() {
        return null;
    }

    public static String getInputURI(String collection){
        return String.format("mongodb://localhost/%s", collection);
    }

    private static HashMap<String, ArrayList<DBObject>> convertToJSONString(String filename) throws FileNotFoundException {
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
                //TODO : delete it
                word = word.replace("\"", "\\\"");
                Integer occurency = (Integer) obj.get("value");
                DBObject document = new BasicDBObject();
                document.put("word", word);
                document.put("occ", occurency);
                //TODO : add the tf calculation (0.5 + 0.5 * (occurency / getOccMax(docId))
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

}
