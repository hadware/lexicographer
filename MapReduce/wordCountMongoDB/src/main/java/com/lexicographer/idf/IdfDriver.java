package com.lexicographer.idf;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.BSONFileOutputFormat;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count MongoDB!
 */
public class IdfDriver extends Configured implements Tool {

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
        String inputURI = MongoUtils.getInputURI(args[0]);
        MongoConfigUtil.setInputURI(getConf(), inputURI);

        Job job = new Job(getConf(), "Idf");
        job.setJarByClass(getClass());

        job.setMapperClass(IdfMapper.class);
        job.setReducerClass(IdfReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        job.setInputFormatClass(MongoInputFormat.class);
        //Car mise à jour de Mongo dans le Reducer
        job.setOutputFormatClass(NullOutputFormat.class);
        System.out.println("Conf: " + getConf());

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        MongoUtils.connect();
        MongoUtils.initIdf();
        int exitCode = ToolRunner.run(new IdfDriver(), args);
        MongoUtils.close();
        System.exit(exitCode);
    }
}
