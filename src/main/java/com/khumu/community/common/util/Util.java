package com.khumu.community.common.util;

public class Util {
    public static String getShortString(String text, int maxLength) {
        if (text == null) {
            return "";
        }
        if (text.length() <= maxLength) {
            return text;
        } else {
            return text.substring(0, maxLength) + "...";
        }
    }
}
