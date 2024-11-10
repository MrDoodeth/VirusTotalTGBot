package com.MyDo.text;

import com.MyDo.tool.JsonReader;

import java.io.File;

public class Configurator {
    private static final StringBuilder path = new StringBuilder()
            .append("src")
            .append(File.separator)
            .append("main")
            .append(File.separator)
            .append("resources")
            .append(File.separator)
            .append("config.json");

    public static String getText(String... keys) {
        return JsonReader.getVariableOfFile(path, keys).toString();
    }
}
