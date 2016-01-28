package com.lexicographer;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.*;
import java.net.URI;
import java.util.*;

public class MongoUtils {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private static MongoClientURIBuilder uriBuilder = new MongoClientURIBuilder();
    static {
        uriBuilder.addHost("epub1-0u278hoc.cloudapp.net", 27017);
        uriBuilder.addHost("epub2-a7q4vt06.cloudapp.net", 27017);
        uriBuilder.addHost("epub3-k16i2rdh.cloudapp.net", 27017);
        uriBuilder.collection("epub", "books");
    }

    public static MongoClientURI buildInputURI() {
        return uriBuilder.build();
    }

    public MongoUtils() {
        mongoClient = new MongoClient(uriBuilder.build());
        db = mongoClient.getDatabase("epub");
    }

    public void updateStat(String statName, String id, int value) {
        updateStat(statName, id, (float) value);
    }

    public void updateStat(String statName, String id, float value) {
        try {
            System.out.println("Updating " + statName + " for doc " + id);
            db.getCollection("bookStats").updateOne(new Document("_id", new ObjectId(id)),
                    new Document("$set", new Document("stats." + statName, value)), new UpdateOptions().upsert(true));
        } catch (MongoException e) {
            System.out.println("Exception updating stat " + statName + " on document " + id + "\nWaiting 5sec before retrying");
            try {
                Thread.sleep(5000);
                db.getCollection("bookStats").updateOne(new Document("_id", new ObjectId(id)),
                        new Document("$set", new Document("stats." + statName, value)), new UpdateOptions().upsert(true));
            } catch (InterruptedException | MongoException e1) {
                System.err.println("Exception updating stat " + statName + " on document " + id + ": " + e1.getMessage());
                System.exit(0);
            }
        }
    }

    public void addWordsGlossary(String filename) throws FileNotFoundException {
        HashMap<String, ArrayList<DBObject>> map = extractInformation(filename);
        if (map != null) {
            for (Map.Entry<String, ArrayList<DBObject>> entry : map.entrySet()) {
                addWordsMongo(entry.getKey(), entry.getValue());
            }
        }
    }

    private void addWordsMongo(String docId, ArrayList<DBObject> documents) {
        try {
            System.out.println("Updating glossary for doc " + docId);
            db.getCollection("glossaries").insertOne(new Document("_id", new ObjectId(docId)).append("glossary", documents));
            System.out.println("Doc inserted " + docId);
        } catch (MongoException e) {
            System.out.println("Exception adding words on document " + docId + "\nWaiting 5sec before retrying");
            try {
                Thread.sleep(5000);
                db.getCollection("glossaries").insertOne(new Document("_id", new ObjectId(docId)).append("glossary", documents));
            } catch (InterruptedException | MongoException e1) {
                System.err.println("Exception adding words on document " + docId + ": " + e1.getMessage());
                System.exit(0);
            }
        }
    }

    private HashMap<String, ArrayList<DBObject>> extractInformation(String filename) throws FileNotFoundException {
        File file = new File(filename);
        String docId;
        HashMap<String, ArrayList<DBObject>> map = new HashMap<>();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));


        BSONDecoder decoder = new BasicBSONDecoder();
        try {
            int count = 0;
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
                    map.put(docId, new ArrayList<>());
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

    public void close() {
        mongoClient.close();
    }

    public void storeIdf(String word, Set<String> ids) {
        try {
            System.out.println("Storing idf");
            DBObject o = new BasicDBObject(word, ids);
            db.getCollection("idf").updateOne(new Document("_id", "idf"), new Document("$set", o));
        } catch (MongoException e) {
            System.out.println("Exception storing idf\nWaiting 5sec before retrying");
            try {
                Thread.sleep(5000);
                DBObject o = new BasicDBObject(word, ids);
                db.getCollection("idf").updateOne(new Document("_id", "idf"), new Document("$set", o));
            } catch (InterruptedException | MongoException e1) {
                System.err.println("Exception storing idf: " + e1.getMessage());
                System.exit(0);
            }
        }
    }

    public void initIdf() {
        try {
            db.getCollection("idf").drop();
        } catch (MongoException e) {
            System.out.println("Exception droping idf\nWaiting 5sec before retrying");
            try {
                Thread.sleep(5000);
                db.getCollection("idf").drop();
            } catch (InterruptedException | MongoException e1) {
                System.err.println("Exception droping idf: " + e1.getMessage());
                System.exit(0);
            }
        }

        try {
            db.getCollection("idf").insertOne(new Document("_id", "idf"));
        } catch (MongoException e) {
            System.out.println("Exception inserting idf\nWaiting 5sec before retrying");
            try {
                Thread.sleep(5000);
                db.getCollection("idf").insertOne(new Document("_id", "idf"));
            } catch (InterruptedException | MongoException e1) {
                System.err.println("Exception inserting idf: " + e1.getMessage());
                System.exit(0);
            }
        }
    }
}
