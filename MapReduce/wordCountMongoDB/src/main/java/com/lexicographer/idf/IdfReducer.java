package com.lexicographer.idf;

import com.lexicographer.MongoUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ahasall on 26/12/15.
 */
public class IdfReducer extends Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Set<String> set = new HashSet<>();

        for (final Text val : values) {
            set.add(val.toString());
        }
        MongoUtils.storeIdf(key.toString(), set);
        Object[] objects = set.toArray();
        context.write(key, new Text(objects.toString()));
    }
}
