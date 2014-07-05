package com.workagram.resume;

/**
 * Created by alexfernandezwhiteskylabs on 7/4/14.
 */
public class ResumeParserApp {
    public static void main(String[] args) {

        String workingDir = System.getProperty("user.dir");
        System.out.println("Current working directory : " + workingDir);
        String resumeFile = workingDir + "/data/CVSample.doc";
        String outputFile = workingDir + "/output/objective.txt";

        IntelligenceReference intelligenceReference = new IntelligenceReference();
        /**
        intelligenceReference.setDelimiters(workingDir + "/intelligence/objective/delimiters.txt");
        intelligenceReference.setKeywords(workingDir + "/intelligence/objective/keywords.txt");
        intelligenceReference.setVerbs(workingDir + "/intelligence/objective/verbs.txt");
         */
        IntelligenceInitializer intelligenceInitializer = new IntelligenceInitializer(intelligenceReference);
       // ObjectiveParser objectiveParser = new ObjectiveParser(resumeFile, outputFile, intelligenceInitializer);
       // objectiveParser.getInfo();
        /**
        intelligenceReference.setDelimiters(workingDir + "/intelligence/personaldeatails/delimiters.txt");
        intelligenceReference.setKeywords(workingDir + "/intelligence/personaldeatails/keywords.txt");
        intelligenceReference.setVerbs(workingDir + "/intelligence/personaldeatails/verbs.txt");
        outputFile = workingDir + "/output/personal_details.txt";

        PersonalDetails personalDetails = new PersonalDetails(resumeFile, outputFile, intelligenceInitializer);
        personalDetails.getInfo();
        */
        intelligenceReference.setDelimiters(workingDir + "/intelligence/education/delimiters.txt");
        intelligenceReference.setKeywords(workingDir + "/intelligence/education/keywords.txt");
        intelligenceReference.setVerbs(workingDir + "/intelligence/education/verbs.txt");
        outputFile = workingDir + "/output/education.txt";
        Education personalDetails = new Education(resumeFile, outputFile, intelligenceInitializer);
        personalDetails.getInfo();


    }

}
