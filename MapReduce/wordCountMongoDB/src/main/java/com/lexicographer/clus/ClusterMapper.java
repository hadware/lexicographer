package com.lexicographer.clus;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by ahasall on 26/12/15.
 */
public class ClusterMapper extends Mapper<Object, BSONObject, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private final Text word = new Text();

    public void map(Object key, BSONObject value, Context context) throws IOException, InterruptedException {
        System.out.println("key: " + key);
        System.out.println("value: " + value);

        final StringTokenizer itr = new StringTokenizer(value.get("x").toString());
        while (itr.hasMoreTokens()) {
            word.set(itr.nextToken());
            context.write(word, one);
        }
    }
}


