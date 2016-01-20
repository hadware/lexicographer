package com.lexicographer.wordSize;

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
public class WordSizeMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            System.out.println(c.get("text"));
            String str = c.getString("text");

            final StringTokenizer itr = new StringTokenizer(str, " \t\n\r\f,;:..!\"()-?");
            while (itr.hasMoreTokens()) {
                identifier.setDocId(key.toString());
                String keyOut = identifier.getDocId();
                Integer tailleMot = itr.nextToken().length();
                System.out.println(tailleMot);
                try {
                    context.write(new Text(keyOut), new IntWritable(tailleMot));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


