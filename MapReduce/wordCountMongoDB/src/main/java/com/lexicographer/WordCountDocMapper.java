package com.lexicographer;

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
public class WordCountDocMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            System.out.println(c.get("text"));
            String str = c.getString("text");
            String[] words = str.split(" de | à | et ");
            for (int i = 0; i < words.length ; i++) {
                final StringTokenizer itr = new StringTokenizer(words[i]," \t\n\r\f,;:..!\"()-?");
                while (itr.hasMoreTokens()) {
                    identifier.setWord(itr.nextToken());
                    identifier.setChapterId(c.getString("name"));
                    identifier.setDocId(key.toString());
                    String keyOut = String.format("(%s,%s,%s)", "idCommun", identifier.getDocId(), -1);
                    try {
                        context.write(new Text(keyOut), one);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }
}


