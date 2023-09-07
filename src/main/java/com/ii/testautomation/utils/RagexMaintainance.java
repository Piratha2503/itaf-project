package com.ii.testautomation.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RagexMaintainance {
    public boolean checkSpaceBeforeAfterWords(String input)
    {
        String regexPattern = "^(?!\\s)(?!.*\\s$)[A-Za-z0-9\\s]+";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches())
            return true;
        else return false;

    }
}
