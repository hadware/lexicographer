package com.lexicographer.cluster;

import com.lexicographer.MongoUtils;
import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.util.MongoClientURIBuilder;
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
 * Word Count MongoDB!
 */
public class ClusterDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {

        final Configuration conf = new Configuration();

        MongoConfigUtil.setInputURI( conf, MongoUtils.buildInputURI());
        //MongoConfigUtil.setOutputURI( conf, "mongodb://localhost/test.out" );
        System.out.println( "Conf: " + conf );

        final Job job = new Job( conf, "word count" );

        job.setJarByClass( ClusterDriver.class );

        job.setMapperClass( ClusterMapper.class );

        job.setReducerClass( ClusterReducer.class );

        job.setOutputKeyClass( Text.class );
        job.setOutputValueClass( IntWritable.class );

        job.setInputFormatClass( MongoInputFormat.class );
        job.setOutputFormatClass( NullOutputFormat.class );

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new ClusterDriver(), args);
        System.exit(exitCode);
    }
}
