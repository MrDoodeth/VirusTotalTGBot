package com.MyDo.tool;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public abstract class JsonReader {
    public static Object getVariableOfFile(StringBuilder path, String... keys) {
        Object variable = null;
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(path.toString()));

            for (String key : keys) {
                variable = jsonObject.get(key);
                if (variable instanceof JSONObject) {
                    jsonObject = (JSONObject) variable;
                } else {
                    break;
                }
            }
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }
        return variable;
    }
}