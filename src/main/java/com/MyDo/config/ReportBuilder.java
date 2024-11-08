package com.MyDo.config;

import org.json.JSONObject;

public class ReportBuilder {
    public static String build(String fileName, JSONObject jsonReport) {
        JSONObject data = jsonReport.getJSONObject("data");
        JSONObject stats = data.getJSONObject("attributes").getJSONObject("stats");
        JSONObject results = data.getJSONObject("attributes").getJSONObject("results");

        StringBuilder report = new StringBuilder()
                .append('*')
                .append(fileName)
                .append('*')
                .append("\r\n");

        //Выводит справку
        for (String key : stats.keySet()) {
            //Исключения
            if (key.equals("confirmed-timeout")) {
                continue;
            }

            report
                    .append(Configurator.getText("report", key))
                    .append('*')
                    .append(stats.getInt(key))
                    .append('*')
                    .append("\r\n");
        }

        //Перегородка
        report
                .append("=========================")
                .append("\r\n");


        //Выводит результат
        for (String key : results.keySet()) {
            report
                    .append(Configurator.getText("report", results.getJSONObject(key).getString("category")).charAt(0))
                    .append('*')
                    .append(key)
                    .append('*')
                    .append("\r\n");
        }

        return report.toString();
    }
}
