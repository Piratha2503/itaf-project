package com.ii.testautomation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isNotNullAndEmpty(String field)
    {
        return field != null && !field.isEmpty();
    }

    public boolean checkRagexBeforeAfterWordsTestCases(String input)
    {
        String regexPattern = "^(?!\\s)[A-Za-z0-9.]+(?:\\s[A-Za-z0-9.]+)*$";

        Pattern pattern = Pattern.compile(regexPattern);

        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkRagexBeforeAfterWords(String input)
    {
        String regexPattern = "^(?!\\\\s)[A-Za-z0-9]+(?:\\\\s[A-Za-z0-9]+)*$";

        Pattern pattern = Pattern.compile(regexPattern);

        Matcher matcher = pattern.matcher(input);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    private Utils()
    {
    }

}
