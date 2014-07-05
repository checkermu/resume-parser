package com.workagram.resume;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 */
public class PersonalInfoParser {
    private ArrayList<String> delimiters;        // list of delimiters that are used when extracting text
    private HashMap<Integer, ArrayList<String>> keywords;        // list of all potential hot keywords
    private ArrayList<String> keywordsFound;                    // list of the hotkeywords found in text
    private ArrayList<String> meaningfulSentences;                // generated list of meaningful sentences
    private ArrayList<String> lines;                // the input text when broken up into several lines
    private ArrayList<String> verbs;                // list of action verbs used to process resumes

    public PersonalInfoParser(String resumeFile, String outputFile, IntelligenceReference intelligenceReference) {
        this.resumeFile = resumeFile;
        this.outputFile = outputFile;
        this.intelligenceReference = intelligenceReference;
        meaningfulSentences = new ArrayList<String>();
        keywordsFound = new ArrayList<String>();
        initDelimiters();
        initKeywords();
        initVerbs();

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

    public IntelligenceReference getIntelligenceReference() {
        return intelligenceReference;
    }

    public void setIntelligenceReference(IntelligenceReference intelligenceReference) {
        this.intelligenceReference = intelligenceReference;
    }

    private IntelligenceReference intelligenceReference;
    // list of informative delimiters that are not included in the delimiters file
    public final static String[] infoDelims = {
            "experience",
            "proficiency",
            "minimum",
            "recent",
            "expertise",
            "proficient",
            "strong",
            "excellent",
    };

    // initializer for arrayList of verbs, extracts data from the verbs file
    private void initVerbs() {
        verbs = new ArrayList<String>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(new File(intelligenceReference.getVerbs())));
            String str = in.readLine();
            while (str != null) {
                verbs.add(str);
                str = in.readLine();
            }
        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + intelligenceReference.getVerbs() + " was not found.");
            System.exit(0);
        }
    }

    // initializer for arrayList of delimiters, extracts data from the delimiters file
    private void initDelimiters() {
        delimiters = new ArrayList<String>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(new File(intelligenceReference.getDelimiters())));
            String str = in.readLine();
            while (str != null) {
                delimiters.add(" " + str + " ");
                str = in.readLine();
            }
            delimiters.add(". ");
            delimiters.add("; ");
        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + intelligenceReference.getDelimiters() + " was not found.");
            System.exit(0);
        }
    }


    private void initKeywords() {
        keywords = new HashMap<Integer, ArrayList<String>>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(new File(intelligenceReference.getKeywords())));
            String str = in.readLine();
            while (str != null) {
                int length = str.split(" ").length;
                if (!keywords.containsKey(new Integer(length))) {
                    keywords.put(new Integer(length), new ArrayList<String>());
                }
                (keywords.get(new Integer(length))).add(str);
                str = in.readLine();
            }
        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + intelligenceReference.getKeywords() + " was not found.");
            System.exit(0);
        }
    }


    // grabs input from the inputFile specified and converts the text into a list of sentences for processing
    public void getInput() {
        BufferedReader in;
        POIFSFileSystem fs = null;
        try {
            fs = new POIFSFileSystem(new FileInputStream(getResumeFile()));
            //Couldn't close the braces at the end as my site did not allow it to close
            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);
            String[] paragraphs = we.getParagraphText();
            String inputText = new String();
            System.out.println("Word Document has " + paragraphs.length + " paragraphs");
            for (int i = 0; i < paragraphs.length; i++) {
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
                inputText += paragraphs[i] + "; ";
                System.out.println(inputText);
            }
            lines = split(inputText);

        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + getResumeFile() + " was not found.");
            System.exit(0);
        }
    }

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
            for (String delim : delimiters) {
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
        for (String str : lines) {
            for (Integer i : new ArrayList<Integer>(keywords.keySet())) {
                if (i == 1) {
                    for (String word : str.split(" ")) {
                        word = cleanUp(word);
                        if (search(word.toLowerCase(), verbs) >= 0) {
                            if (!meaningfulSentences.contains(str)) {
                                meaningfulSentences.add(str);
                            }
                        }
                        if (search(word.toLowerCase(), keywords.get(new Integer(1))) >= 0) {
                            if (!keywordsFound.contains(word.toLowerCase())) {
                                keywordsFound.add(word.toLowerCase());
                            }
                            if (!meaningfulSentences.contains(str) && str.split(" ").length > 2) {
                                meaningfulSentences.add(str);
                            }
                        }
                    }
                } else {
                    for (String word : keywords.get(i)) {
                        if (str.toLowerCase().contains(word.toLowerCase()) && !keywordsFound.contains(word)) {
                            if (!keywordsFound.contains(word.toLowerCase())) {
                                keywordsFound.add(word.toLowerCase());
                            }
                            if (!meaningfulSentences.contains(str) && str.split(" ").length > 2) {
                                meaningfulSentences.add(str);
                            }
                        }
                    }
                }
            }
            for (String delim : infoDelims) {
                if (str.toLowerCase().contains(delim.toLowerCase())) {
                    if (!meaningfulSentences.contains(str) && str.split(" ").length > 2) {
                        meaningfulSentences.add(str);
                    }
                }
            }
        }
        Collections.sort(keywordsFound);
    }

    // method that is used to display the output to the console
    private void display() {
        System.out.println("Meaningful Sentences: ");
        System.out.println("=====================");
        for (String str : meaningfulSentences) {
            System.out.println(reduceSpace(str));
        }
        System.out.println("\n\n");
        System.out.println("Hot Keywords:");
        System.out.println("=============");
        for (String str : keywordsFound) {
            System.out.println(str);
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
                for (String str : meaningfulSentences) {
                    out.write(reduceSpace(str) + "\r\n");
                }
                out.write("\r\n\r\n");
                out.write("Hot Keywords:" + "\r\n");
                out.write("=============" + "\r\n");
                for (String str : keywordsFound) {
                    out.write(str + "\r\n");
                }
                out.close();
                System.out.println("The output has been written to " + outputFile);
            } catch (IOException e) {
                System.out.println("An error occurred while writing to file: " + outputFile);
                System.out.println("The results have instead been displayed below.");
                display();
            }
        }
    }

    // reduces enormous amounts of middle space found in between strings and recursiely reduces it into on space
    private String reduceSpace(String s) {
        if (!s.contains("  ")) {
            return s;
        }
        return reduceSpace(s.replace("  ", " "));

    }

    public void getParseInfo() {
        long start = System.currentTimeMillis();
        this.getInput();
        this.analyze();
        this.output();
        this.display();
        long end = System.currentTimeMillis();

        System.out.println("\n\nTime taken: " + (end - start) + "ms");
    }
}
