package com.lexicographer;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Created by ahasall on 26/12/15.
 */
public class WordBySentenceMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    //Le variable d'entrée du couple produit est en Text : IdObject + numero de la phrase dans le livre
    private final static IntWritable nbrWords = new IntWritable();
    private final WDCIdentifier identifier = new WDCIdentifier();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        identifier.setDocId(key.toString());
        // Récupération de la liste des chapitres
        ArrayList<BasicDBObject> chapters = (ArrayList<BasicDBObject>) value.get("chapters");
        // Pour chaque chapitre on récupère le WCDIdentifier...
        chapters.forEach(c -> {
            String[] tabSentence = c.getString("text").split("\\. | \\. |\\? | \\? |! | ! ");

            for (String s : tabSentence) {
                nbrWords.set(s.split(" |:").length);
                try {
                    context.write(new Text(identifier.getDocId()), nbrWords);
                    System.out.println(identifier.getDocId() + "," + nbrWords);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


