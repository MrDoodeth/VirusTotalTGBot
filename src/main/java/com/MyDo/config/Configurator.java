package com.MyDo.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return variable;
    }
}
