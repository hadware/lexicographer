package com.lexicographer;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by ahasall on 26/12/15.
 */
public class WordCountMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            System.out.println(c.get("text"));
            final StringTokenizer itr = new StringTokenizer(c.getString("text"));
            while (itr.hasMoreTokens()) {
                identifier.setWord(itr.nextToken());
                identifier.setChapterId(c.getString("name"));
                identifier.setDocId(key.toString());
                String keyOut = String.format("(%s,%s,%s)", identifier.getWord(), identifier.getDocId(), identifier.getChapterId());
                try {
                    context.write(new Text(keyOut), one);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


