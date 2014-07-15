package com.workagram.resume;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 */
public class Education {
    private ArrayList<String> objective;                // generated list of meaningful sentences
    private ParseHelper parseHelper;
    private IntelligenceInitializer intelligenceInitializer;
    private ParserUtils parserUtils;

    public IntelligenceInitializer getIntelligenceInitializer() {
        return intelligenceInitializer;
    }

    public void setIntelligenceInitializer(IntelligenceInitializer intelligenceInitializer) {
        this.intelligenceInitializer = intelligenceInitializer;
    }

    public Education(String resumeFile, String outputFile, IntelligenceInitializer intelligenceInitializer) {
        this.resumeFile = resumeFile;
        this.outputFile = outputFile;
        this.intelligenceInitializer = intelligenceInitializer;
        parseHelper = new ParseHelper(resumeFile, outputFile);
        objective = new ArrayList<String>();
        parserUtils = new ParserUtils();
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }

    private String resumeFile;


    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    private String outputFile;

    // used to process the string text originally converted into one massive string
    // this method breaks up the input text into various lines based on the delimiters specified  and returns an ArrayList of the resulting lines
    public ArrayList<String> split(String str) {
        ArrayList<String> lines = new ArrayList<String>();
        ArrayList<Integer> splice = new ArrayList<Integer>();
        HashMap<Integer, String> spliceLocs = new HashMap<Integer, String>();
        if (str == null || str.length() == 0) {
            return lines;
        }
        do {
            for (String delim : intelligenceInitializer.getDelimiters()) {
                int i = str.indexOf(delim);
                if (i >= 0) {
                    splice.add(new Integer(i));
                    spliceLocs.put(new Integer(i), delim);
                }
            }
            Collections.sort(splice);
            lines.add(parserUtils.cleanUp(str.substring(0, splice.get(0))));
            str = str.substring(splice.get(0) + spliceLocs.get(splice.get(0)).length());
            splice.clear();
            spliceLocs.clear();
        } while (str.length() > 0);
        return lines;
    }


    // analyzes the list of lines and finds hot keywords and well as meaningful sentences
    public void analyze() {
        int line = 0;
        for (String str : split(parseHelper.getParagraphs())) {
            line++;
            for (Integer i : new ArrayList<Integer>(intelligenceInitializer.getKeywords().keySet())) {
                if (i == 1) {
                    for (String word : str.split(" ")) {
                        word = parserUtils.cleanUp(word);
                        if (parserUtils.search(word.toLowerCase(), intelligenceInitializer.getDummyVerbs()) >= 0) {
                            if (!objective.contains(str)) {
                                objective.add(str);
                                arrayList.add(line);
                            }
                        }

                    }
                }
            }
        }
        analyzeSchool(arrayList);
        splitSchool();
    }

    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private ArrayList<String> arrayListUniversity = new ArrayList<String>();
    //Todo Find

    public void analyzeSchool(ArrayList<Integer> lineNos) {
        ArrayList<String> paragraphs = split(parseHelper.getParagraphs());
        ArrayList<String> dummySchoolRelated = new ArrayList<String>();
        dummySchoolRelated.add("University");
        dummySchoolRelated.add("college");
        dummySchoolRelated.add("school");
        for (int i : lineNos) {
            int dummyLines = i - 10;
            for (int loopCurrentCummyLines = dummyLines; loopCurrentCummyLines < i; loopCurrentCummyLines++) {
                //System.out.println(paragraphs.get(loopCurrentCummyLines));
                for (String word : paragraphs.get(loopCurrentCummyLines).split(" ")) {
                    word = parserUtils.cleanUp(word).trim();
                    for (String keyword : dummySchoolRelated) {
                        if (word.toLowerCase().trim().contains(keyword.toLowerCase())) {
                            if (!arrayListUniversity.contains(paragraphs.get(loopCurrentCummyLines))) {
                                arrayListUniversity.add(paragraphs.get(loopCurrentCummyLines));
                            }

                        }
                    }


                }
            }
        }
    }

    private void splitSchool() {
        for (String line : arrayListUniversity) {
            // System.out.println(parseHelper.reduceSpace(line));
            int i = 1;
            if (line.lastIndexOf("University") > 0) {

                String studiedDate = line.substring(0, line.lastIndexOf("University"));
                String educationDate = line.substring(line.lastIndexOf("University"));
                System.out.println(parseHelper.reduceSpace(studiedDate).trim());
                System.out.println(parseHelper.reduceSpace(objective.get(i)));
                System.out.println(parseHelper.reduceSpace(educationDate).trim());
                i++;
            }

        }
    }

    // method that is used to display the output to the console
    private void display() {
        System.out.println("Education");
        System.out.println("=====================");
        for (String str : objective) {

        }

    }

    // main output method that determines whether to display output to console or write output to a file
    public void output() {
        if (outputFile.length() == 0) {
            display();
        } else {
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(new File(outputFile)));
                out.write("Meaningful Sentences:" + "\r\n");
                out.write("=====================" + "\r\n");
                for (String str : objective) {
                    out.write(parseHelper.reduceSpace(str) + "\r\n");
                }
                System.out.println("The output has been written to " + outputFile);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to file: " + outputFile);
                System.out.println("The results have instead been displayed below.");
                display();
            }
        }
    }

    public void getInfo() {
        long start = System.currentTimeMillis();
        this.analyze();
        this.output();
        this.display();
        long end = System.currentTimeMillis();

        System.out.println("\n\nTime taken: " + (end - start) + "ms");
    }
}
