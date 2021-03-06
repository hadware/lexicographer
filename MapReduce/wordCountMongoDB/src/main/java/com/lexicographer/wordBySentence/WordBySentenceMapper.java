package com.lexicographer.wordBySentence;

import com.lexicographer.WDCIdentifier;
import com.mongodb.BasicDBObject;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


