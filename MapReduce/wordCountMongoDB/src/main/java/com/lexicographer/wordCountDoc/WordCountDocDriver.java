package com.lexicographer.wordCountDoc;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count MongoDB!
 * Nombre moyen de mots dans les livres
 */
public class WordCountDocDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <inputDB> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        setConf(new Configuration());
        String inputURI = String.format("mongodb://localhost/%s", args[0]);
//      String outputURI = String.format("mongodb://localhost/%s", args[1]);
        MongoConfigUtil.setInputURI(getConf(), inputURI);
//           MongoConfigUtil.setOutputURI(getConf(), "mongodb://localhost/test.out");


        Job job = new Job(getConf(), "Word Size MongoDB PASS 1");
        job.setJarByClass(getClass());

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(WordCountDocMapper.class);
        job.setCombinerClass(WordCountDocReducer.class);
        job.setReducerClass(WordCountDocReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setInputFormatClass(MongoInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        System.out.println("Conf: " + getConf());

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountDocDriver(), args);
        System.exit(exitCode);
    }
}
