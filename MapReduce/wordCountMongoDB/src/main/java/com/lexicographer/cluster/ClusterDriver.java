package com.lexicographer.cluster;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
import com.mongodb.hadoop.util.MongoConfigUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Word Count MongoDB!
 */
public class ClusterDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {

        final Configuration conf = new Configuration();
        MongoClientURIBuilder uriBuilder =new MongoClientURIBuilder();
        uriBuilder.addHost("epub1-0u278hoc.cloudapp.net", 27017);
        uriBuilder.addHost("epub2-a7q4vt06.cloudapp.net", 27017);
        uriBuilder.addHost("epub3-k16i2rdh.cloudapp.net", 27017);
/*
        uriBuilder.addHost("localhost",27017);
*/
        uriBuilder.collection("test", "in");

        MongoConfigUtil.setInputURI( conf, uriBuilder.build());

        MongoConfigUtil.setOutputURI( conf, "mongodb://localhost/test.out" );
        System.out.println( "Conf: " + conf );

        final Job job = new Job( conf, "word count" );

        job.setJarByClass( ClusterDriver.class );

        job.setMapperClass( ClusterMapper.class );

        job.setReducerClass( ClusterReducer.class );

        job.setOutputKeyClass( Text.class );
        job.setOutputValueClass( IntWritable.class );

        job.setInputFormatClass( MongoInputFormat.class );
        job.setOutputFormatClass( MongoOutputFormat.class );

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new ClusterDriver(), args);
        System.exit(exitCode);
    }
}
