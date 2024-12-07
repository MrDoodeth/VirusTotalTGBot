package com.MyDo.tool;

import com.MyDo.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.Map;

public class ReportBuilder {
    private static final int COUNT = 25;

    public static String build(String entityName, JSONObject jsonReport) {
        JSONObject data = jsonReport.getJSONObject("data");
        JSONObject stats = data.getJSONObject("attributes").getJSONObject("stats");
        JSONObject results = data.getJSONObject("attributes").getJSONObject("results");

        StringBuilder report = new StringBuilder()
                .append('*')
                .append(entityName)
                .append('*')
                .append("\r\n");

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> reportMap = mapper.convertValue(Config.getINSTANCE().getReport(), Map.class);

        //Выводит справку
        for (String key : stats.keySet()) {
            //Исключения
            if (key.equals("confirmed-timeout")) {
                continue;
            }

            report
                    .append(reportMap.get(key))
                    .append('*')
                    .append(stats.getInt(key))
                    .append('*')
                    .append("\r\n");
        }

        //Перегородка
        report
                .append("=".repeat(COUNT))
                .append("\r\n");


        //Выводит результат
        for (String key : results.keySet()) {
            report
                    .append(reportMap.get(results.getJSONObject(key).getString("category")).charAt(0))
                    .append(' ')
                    .append('*')
                    .append(key)
                    .append('*')
                    .append("\r\n");
        }

        return report.toString();
    }
}