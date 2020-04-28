package com.example.mymovies.utils;

public class Tools {
    public static String truncate(String s, int maxLength) {
        if (s.length() > maxLength) {
            s = s.substring(0, Math.min(s.length(), maxLength));
            s += "...";
        }

        return s;
    }
}
