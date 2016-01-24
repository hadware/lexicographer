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
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        identifier.setDocId(key.toString());
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            String str = c.getString("text");
            String[] words = str.split("\\. | \\. |\\? | \\? |! | ! ");
            try {
                context.write(new Text(identifier.getDocId()), new IntWritable(words.length));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}


