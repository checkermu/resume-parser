package com.workagram.resume;

/**
 * Created by alexfernandezwhiteskylabs on 7/5/14.
 * Class responsible for getting the path of the files for Intelligence
 */
public class IntelligenceReference {

    public String getDelimiters() {
        return delimiters;
    }

    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getVerbs() {
        return verbs;
    }

    public void setVerbs(String verbs) {
        this.verbs = verbs;
    }

    private String delimiters;
    private String keywords;
    private String verbs;



}
