package com.lexicographer;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count MongoDB!
 */
public class WordBySentenceDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.printf("Usage: %s [generic options] <inputDB>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        setConf(new Configuration());
        String inputURI = String.format("mongodb://localhost/%s", args[0]);

        MongoConfigUtil.setInputURI(getConf(), inputURI);
        MongoConfigUtil.setOutputURI(getConf(), inputURI);

        Job job = new Job(getConf(), "Word Count MongoDB PASS 1");
        job.setJarByClass(getClass());

        //FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(WordBySentenceMapper.class);
        job.setCombinerClass(WordBySentenceReducer.class);
        job.setReducerClass(WordBySentenceReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(NullOutputFormat.class);

        System.out.println("Conf: " + getConf());

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordBySentenceDriver(), args);
        System.exit(exitCode);
    }
}
