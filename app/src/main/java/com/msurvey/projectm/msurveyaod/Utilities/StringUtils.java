package com.msurvey.projectm.msurveyaod.Utilities;

import java.util.regex.*;

public class StringUtils {

    //Mon Oct 03 09:47:27 UTC 2016

    private static String dateRegex = "([A-Za-z]{3})\\s([A-Za-z]{3})\\s([0-9]{2})\\s([0-9]{2}):([0-9]{2}):([0-9]{2})\\s([A-Z]{2,3})\\s([0-9]{4})";

    private static String month = "[A-Za-z]{3}";

    private static String year = "[0-9]{4}";

    public static String giveFeedback = "Java Coffee House";

    public static String howWasYourExperience = "How was your experience at " + giveFeedback + "?";

    public static String tellUsSomething = "Tell us about it";

    public static String userName = "";

    public static String monthRegexChecker(String stringToCheck){

        String monthRegex = "[A-Za-z]{3}";

        Pattern checkRegex = Pattern.compile(monthRegex);

        Matcher regexMatcher = checkRegex.matcher(stringToCheck);

        return regexMatcher.group(2);

    }

    public static String yearRegexChecker(String stringToCheck){

        String yearRegex = "[0-9]{4}";

        Pattern checkRegex = Pattern.compile(yearRegex);

        Matcher regexMatcher = checkRegex.matcher(stringToCheck);

        return regexMatcher.group();
    }

    public static String regexChecker(String stringToCheck, String regexPattern){

        Pattern checkRegex = Pattern.compile(regexPattern);

        Matcher regexMatcher = checkRegex.matcher(stringToCheck);

        if(regexMatcher.find()){
            return regexMatcher.group();
        } else {
            return "nothing";
        }
    }

}
