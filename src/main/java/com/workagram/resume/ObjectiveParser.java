package com.workagram.resume;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 */
public class ObjectiveParser {
    private ArrayList<String> objective;                // generated list of meaningful sentences
    private ParseHelper parseHelper;
    private IntelligenceInitializer intelligenceInitializer;

    public IntelligenceInitializer getIntelligenceInitializer() {
        return intelligenceInitializer;
    }

    public void setIntelligenceInitializer(IntelligenceInitializer intelligenceInitializer) {
        this.intelligenceInitializer = intelligenceInitializer;
    }

    public ObjectiveParser(String resumeFile, String outputFile, IntelligenceInitializer intelligenceInitializer) {
        this.resumeFile = resumeFile;
        this.outputFile = outputFile;
        this.intelligenceInitializer = intelligenceInitializer;
        parseHelper = new ParseHelper(resumeFile,outputFile);
        objective = new ArrayList<String>();
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


    // list of informative delimiters that are not included in the delimiters file
    public final static String[] infoDelims = {
            "experience",
            "proficiency",
            "minimum",
            "recent",
            "expertise",
            "proficient",
            "strong"
    };


    // recursively removes trailing punctation or anything that is not a letter or number
    private String cleanUp(String str) {
        if (str.length() == 0) {
            return "";
        }
        String ending = str.substring(str.length() - 1);
        if (ending.matches("^[a-zA-Z0-9_]*$")) {
            return str;
        }
        return cleanUp(str.substring(0, str.length() - 1));
    }

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
            lines.add(cleanUp(str.substring(0, splice.get(0))));
            str = str.substring(splice.get(0) + spliceLocs.get(splice.get(0)).length());
            splice.clear();
            spliceLocs.clear();
        } while (str.length() > 0);
        return lines;
    }

    // binary search, used to determine if a word is a hot keyword or not, or if a verb is an action verb
    // both the verb file and the keywords file are kept in alphabetical order to maintain binary search capability
    private int search(String s, ArrayList<String> list) {
        return binarySearch_helper(s, list, 0, list.size() - 1);
    }

    // helper function to implement binary search
    private int binarySearch_helper(String s, ArrayList<String> list, int start, int end) {
        if (end < start) {
            return -1;
        }
        s = s.toLowerCase();
        int middle = (start + end) / 2;
        if (s.equals(list.get(middle).toLowerCase())) {
            return middle;
        } else if (list.get(middle).toLowerCase().compareTo(s) < 0) {
            return binarySearch_helper(s, list, middle + 1, end);
        } else {
            return binarySearch_helper(s, list, start, middle - 1);
        }
    }

    // analyzes the list of lines and finds hot keywords and well as meaningful sentences
    public void analyze() {

        for (String str : split(parseHelper.getParagraphs())) {
            for (Integer i : new ArrayList<Integer>(intelligenceInitializer.getKeywords().keySet())) {
                if (i == 1) {
                    for (String word : str.split(" ")) {
                        word = cleanUp(word);
                        if (search(word.toLowerCase(), intelligenceInitializer.getVerbs()) >= 0) {
                            if (!objective.contains(str)) {
                                objective.add(str);
                            }
                        }

                    }
                }
            }
            for (String delim : infoDelims) {
                if (str.toLowerCase().contains(delim.toLowerCase())) {
                    if (!objective.contains(str) && str.split(" ").length > 2) {
                        objective.add(str);
                    }
                }
            }
        }
    }

    // method that is used to display the output to the console
    private void display() {
        System.out.println("Objective");
        System.out.println("=====================");
        for (String str : objective) {
            System.out.println(parseHelper.reduceSpace(str));
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
