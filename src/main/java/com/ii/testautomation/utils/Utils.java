package com.ii.testautomation.utils;

public class Utils {
    public static boolean isNotNullAndEmpty(String field)
    {
        return field != null && !field.isEmpty();
    }
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    private Utils()
    {
    }

}
