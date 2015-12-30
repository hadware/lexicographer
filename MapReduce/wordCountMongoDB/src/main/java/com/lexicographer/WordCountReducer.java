package com.lexicographer;

import com.mongodb.hadoop.io.BSONWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import java.io.IOException;

/**
 * Created by ahasall on 26/12/15.
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
    private final IntWritable result = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for ( final IntWritable val : values ){
            sum += val.get();
        }
        result.set( sum );
        context.write(key, result );
    }
}
