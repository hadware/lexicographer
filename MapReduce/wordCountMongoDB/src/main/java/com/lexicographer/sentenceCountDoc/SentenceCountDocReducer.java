package com.lexicographer.sentenceCountDoc;

import com.lexicographer.MongoUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by ahasall on 26/12/15.
 */
public class SentenceCountDocReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
    private final IntWritable result = new IntWritable();
    private MongoUtils mongo = new MongoUtils();



    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for ( final IntWritable val : values ){
            sum += val.get();
        }
        result.set( sum );
        mongo.updateStat("nbrSentence", key.toString(), result.get());
    }
}
