package com.MyDo.config;

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
        return JsonReaderTool.getVariableOfFile(path, keys).toString();
    }
}
