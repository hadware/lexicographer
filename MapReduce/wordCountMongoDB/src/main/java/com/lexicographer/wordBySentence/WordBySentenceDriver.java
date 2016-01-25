package com.lexicographer.wordBySentence;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Count the average of the number of words by sentence in a book
 * ALSO Count the number of words in a book
 */
public class WordBySentenceDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        System.out.println("Starting WordBySentence MapReduce...");

        final Configuration conf = new Configuration();
        MongoConfigUtil.setInputURI( conf, MongoUtils.buildInputURI());

        Job job = new Job(conf, "Word Count MongoDB PASS 1");
        job.setJarByClass(getClass());

        job.setMapperClass(WordBySentenceMapper.class);
        job.setReducerClass(WordBySentenceReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(NullOutputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        MongoUtils.connect();
        int exitCode = ToolRunner.run(new WordBySentenceDriver(), args);
        MongoUtils.close();
        System.exit(exitCode);
    }
}
