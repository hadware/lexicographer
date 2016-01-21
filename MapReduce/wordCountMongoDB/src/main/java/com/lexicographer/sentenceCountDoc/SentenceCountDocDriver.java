package com.lexicographer.sentenceCountDoc;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
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
 * Nombre moyen de phrases dans les livres
 */
public class SentenceCountDocDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        System.out.println("Starting SentenceCountDoc MapReduce...");
        if (args.length != 1) {
            System.err.printf("Usage: %s [generic options] <inputDB>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        setConf(new Configuration());
        String inputURI = MongoUtils.getInputURI(args[0]);
        MongoConfigUtil.setInputURI(getConf(), inputURI);

        Job job = new Job(getConf(), "Word Count MongoDB PASS 1");
        job.setJarByClass(getClass());

        job.setMapperClass(SentenceCountDocMapper.class);
        job.setCombinerClass(SentenceCountDocReducer.class);
        job.setReducerClass(SentenceCountDocReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(NullOutputFormat.class);
        System.out.println("Conf: " + getConf());

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new SentenceCountDocDriver(), args);
        System.exit(exitCode);
    }
}
