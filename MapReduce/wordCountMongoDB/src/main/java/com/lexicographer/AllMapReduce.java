package com.lexicographer;

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
        try {
            MongoUtils.connect();
            int exitCode = ToolRunner.run(new WordBySentenceDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);

            exitCode = ToolRunner.run(new SentenceCountDocDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);

            exitCode = ToolRunner.run(new WordCountDriver(), args);
            MongoUtils.addWordsGlossary("output/part-r-00000.bson");
            if (exitCode != 0)
                System.exit(exitCode);

            exitCode = ToolRunner.run(new WordSizeDriver(), args);
            if (exitCode != 0)
                System.exit(exitCode);
        } finally {
            MongoUtils.close();
        }
    }
}

