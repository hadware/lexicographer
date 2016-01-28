package com.lexicographer;

import com.lexicographer.idf.IdfDriver;
import com.lexicographer.sentenceCountDoc.SentenceCountDocDriver;
import com.lexicographer.wordBySentence.WordBySentenceDriver;
import com.lexicographer.wordCount.WordCountDriver;
import com.lexicographer.wordSize.WordSizeDriver;
import org.apache.hadoop.util.ToolRunner;

/**
 * Created by robin on 14/01/16.
 */
public class AllMapReduce {

    public static void main(String[] args) throws Exception {
        long ti = System.currentTimeMillis();
        try {
            MongoUtils mongo = new MongoUtils();
            System.out.println("running Word By Sentence Driver ");
            int exitCode = ToolRunner.run(new WordBySentenceDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);
            System.out.println("Word By Sentence Driver : " + (System.currentTimeMillis() - ti));

            System.out.println("running Sentence Count Doc Driver ");
            exitCode = ToolRunner.run(new SentenceCountDocDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);
            System.out.println("Sentence Count Doc : " + (System.currentTimeMillis() - ti));


            System.out.println("running Word Count Driver ");
            exitCode = ToolRunner.run(new WordCountDriver(), args);
            mongo.addWordsGlossary("output/part-r-00000.bson");
            if (exitCode != 0)
                System.exit(exitCode);
            System.out.println("Word Count Driver : " + (System.currentTimeMillis() - ti));

            System.out.println("running Word Size Driver ");
            exitCode = ToolRunner.run(new WordSizeDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);
            System.out.println("Word Size Driver : " + (System.currentTimeMillis() - ti));

            System.out.println("running IDF Driver ");
            mongo.initIdf();
            exitCode = ToolRunner.run(new IdfDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);
            System.out.println("IDF Driver : " + (System.currentTimeMillis() - ti));
        } finally {

        }
    }
}

