package com.workagram.resume;

import java.util.ArrayList;

/**
 * Created by alexfernandezwhiteskylabs on 7/15/14.
 */
public class ParserUtils {
    // binary search, used to determine if a word is a hot keyword or not, or if a verb is an action verb
    // both the verb file and the keywords file are kept in alphabetical order to maintain binary search capability
    public int search(String s, ArrayList<String> list) {
        return binarySearch_helper(s, list, 0, list.size() - 1);
    }

    // helper function to implement binary search
    public int binarySearch_helper(String s, ArrayList<String> list, int start, int end) {
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



    // recursively removes trailing punctation or anything that is not a letter or number
    public String cleanUp(String str) {
        if (str.length() == 0) {
            return "";
        }
        String ending = str.substring(str.length() - 1);
        if (ending.matches("^[a-zA-Z0-9_]*$")) {
            return str;
        }
        return cleanUp(str.substring(0, str.length() - 1));
    }
}
