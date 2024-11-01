package com.mydo.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;

public class Configurator {
    public static Object getVariable(String... keys) {
        Object variable = null;
        try {
            String path = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "config.json";
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(path));

            for (String key : keys) {
                variable = jsonObject.get(key);
                if (variable instanceof JSONObject) {
                    jsonObject = (JSONObject) variable;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return variable;
    }
}
