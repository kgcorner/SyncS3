package com.kgcorner;

public class Strings {
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().length()<1;
    }
}
