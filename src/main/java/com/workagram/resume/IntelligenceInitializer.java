package com.workagram.resume;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 */
public class IntelligenceInitializer {

    private HashMap<Integer, ArrayList<String>> keywords;

    private ArrayList<String> verbs;

    private IntelligenceReference intelligenceReference;

    private ArrayList<String> delimiters;

    public IntelligenceInitializer(IntelligenceReference intelligenceReference) {
        this.intelligenceReference = intelligenceReference;
    }

    public ArrayList<String> getDelimiters() {
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
        return delimiters;
    }

    public void setDelimiters(ArrayList<String> delimiters) {
        this.delimiters = delimiters;
    }

    public HashMap<Integer, ArrayList<String>> getKeywords() {
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
            this.setKeywords(keywords);
        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + intelligenceReference.getKeywords() + " was not found.");
            System.exit(0);
        }
        return keywords;
    }

    public void setKeywords(HashMap<Integer, ArrayList<String>> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getVerbs() {
        verbs = new ArrayList<String>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(new File(intelligenceReference.getVerbs())));
            String str = in.readLine();
            while (str != null) {
                verbs.add(str);
                str = in.readLine();
            }
            this.setVerbs(verbs);
        }
        // if the file is not found, the program displays an error message and exits
        catch (IOException e) {
            System.out.println("The file " + intelligenceReference.getVerbs() + " was not found.");
            System.exit(0);
        }
        return verbs;
    }

    public void setVerbs(ArrayList<String> verbs) {
        this.verbs = verbs;
    }

    public IntelligenceReference getIntelligenceReference() {
        return intelligenceReference;
    }

    public void setIntelligenceReference(IntelligenceReference intelligenceReference) {
        this.intelligenceReference = intelligenceReference;


    }


}
