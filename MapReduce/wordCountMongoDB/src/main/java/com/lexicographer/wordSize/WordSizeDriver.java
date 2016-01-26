package com.lexicographer.wordSize;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.net.SimpleSocketServer;

/**
 * Word Count MongoDB!
 * Taille moyenne des mots
 */
public class WordSizeDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        System.out.println("Starting WordSize MapReduce...");

        final Configuration conf = new Configuration();
        MongoConfigUtil.setInputURI( conf, MongoUtils.buildInputURI());

        Job job = new Job(conf, "Word Size MongoDB PASS 1");
        job.setJarByClass(getClass());

        job.setMapperClass(WordSizeMapper.class);
        job.setCombinerClass(WordSizeReducer.class);
        job.setReducerClass(WordSizeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(NullOutputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordSizeDriver(), args);
        System.exit(exitCode);
    }
}
