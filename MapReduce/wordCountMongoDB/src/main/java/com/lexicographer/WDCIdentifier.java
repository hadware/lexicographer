package com.lexicographer;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by ahasall on 26/12/15.
 */
public class WDCIdentifier implements WritableComparable {
    String word;
    String docId;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public WDCIdentifier(String word, String docId, String chapterId, int count) {
        this.word = word;
        this.docId = docId;
    }

    public WDCIdentifier(String idWDCIdentifier) {
        String[] tabFields = idWDCIdentifier.split("-");
        this.docId = tabFields[0];
        this.word = tabFields[1];
    }

    public String convertString() {
        return this.docId + '-' + this.word;
    }

    public WDCIdentifier() {

    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.word);
        dataOutput.writeUTF(this.docId);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.word = dataInput.readUTF();
        this.docId = dataInput.readUTF();
    }
}
