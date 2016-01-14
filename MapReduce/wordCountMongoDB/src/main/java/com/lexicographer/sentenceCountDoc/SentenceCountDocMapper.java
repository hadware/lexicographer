package com.lexicographer.sentenceCountDoc;

import com.lexicographer.WDCIdentifier;
import com.mongodb.BasicDBObject;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by ahasall on 26/12/15.
 */
public class SentenceCountDocMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            System.out.println(c.get("text"));
            String str = c.getString("text");
            String[] words = str.split("\\. | \\. |\\? | \\? |! | ! ");

            try {
                context.write(new Text(key.toString()), new IntWritable(words.length));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}


