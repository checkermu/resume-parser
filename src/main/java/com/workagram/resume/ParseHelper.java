package com.workagram.resume;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 * Responsible for Parsing the
 */
public class ParseHelper {

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }

    public ParseHelper(String resumeFile, String outputFile) {
        this.resumeFile = resumeFile;
        this.outputFile = outputFile;
    }

    public String getParagraphs() {
        BufferedReader in;
        POIFSFileSystem fs = null;
        String inputText = new String();
        try {
            fs = new POIFSFileSystem(new FileInputStream(getResumeFile()));
            //Couldn't close the braces at the end as my site did not allow it to close
            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            for (int i = 0; i < paragraphs.length; i++) {
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
               // System.out.println(paragraphs[i]);
                inputText += paragraphs[i] + "; ";
            }

        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + getResumeFile() + " was not found.");
            System.exit(0);
        }
        return inputText;
    }

    public String reduceSpace(String s) {
        if (!s.contains("  ")) {
            return s;
        }
        return reduceSpace(s.replace("  ", " "));

    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }

    private ArrayList<String> lines;
    private String resumeFile;
    private String outputFile;


}
