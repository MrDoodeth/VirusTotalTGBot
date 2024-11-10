package com.MyDo.tool;

import java.net.URL;

public abstract class UrlValidator {
    public static boolean isValidUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}