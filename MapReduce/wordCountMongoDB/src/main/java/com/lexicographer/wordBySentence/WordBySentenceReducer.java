package com.lexicographer.wordBySentence;

import com.lexicographer.MongoUtils;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by ahasall on 26/12/15.
 */
public class WordBySentenceReducer extends Reducer<Text, IntWritable, Text, FloatWritable>{
    private final FloatWritable result = new FloatWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        int nbrSentences = 0;
        for ( final IntWritable val : values ){
            sum += val.get();
            nbrSentences++;
        }
        result.set((float)sum / (float)nbrSentences);

        System.out.println("Updating nbrWordBySentence and nbrWord for doc " + key.toString());
        MongoUtils.updateStat("nbrWordBySentence", key.toString(), result.get());
        MongoUtils.updateStat("nbrWord", key.toString(), sum);
    }
}
