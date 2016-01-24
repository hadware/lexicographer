package com.lexicographer.wordCount;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.BSONFileOutputFormat;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count MongoDB!
 */
public class WordCountDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        System.out.println("Starting WordCount MapReduce...");
        if (args.length != 1) {
            System.err.printf("Usage: %s [generic options] <inputDB>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        setConf(new Configuration());
        
       // String inputURI = String.format("mongodb://localhost:27021", args[0]);
        String inputURI = "mongodb://localhost:27017/epub.books";

        System.out.println("string : " + inputURI);

//      String outputURI = String.format("mongodb://localhost/%s", args[1]);

        MongoConfigUtil.setInputURI(getConf(), inputURI);

        Job job = new Job(getConf(), "Word Count MongoDB PASS 1");
        job.setJarByClass(getClass());

        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileOutputFormat.setOutputPath(job, new Path("output"));

        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(BSONFileOutputFormat.class);
        System.out.println("Conf: " + getConf());

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountDriver(), args);
        MongoUtils.connect();
        MongoUtils.addWordsGlossary("output/part-r-00000.bson");
        MongoUtils.close();
        System.exit(exitCode);
    }
}
