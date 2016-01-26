package com.lexicographer.wordSize;

import com.lexicographer.MongoUtils;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by ahasall on 26/12/15.
 */
public class WordSizeReducer extends Reducer<Text, IntWritable, Text, FloatWritable>{
    private final FloatWritable result = new FloatWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        int nb = 0;
        for ( final IntWritable val : values ){
            sum += val.get();
            nb++;
        }
        result.set( (float)sum / (float)nb  );
        MongoUtils.connect();
        MongoUtils.updateStat("meanWordSize", key.toString(), result.get());
        MongoUtils.close();
    }
}
