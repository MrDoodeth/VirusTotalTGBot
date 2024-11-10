package com.MyDo.tool;

import org.json.JSONObject;

import static com.MyDo.text.Configurator.getText;

public abstract class ReportBuilder {
    public static String build(String entityName, JSONObject jsonReport) {
        JSONObject data = jsonReport.getJSONObject("data");
        JSONObject stats = data.getJSONObject("attributes").getJSONObject("stats");
        JSONObject results = data.getJSONObject("attributes").getJSONObject("results");

        StringBuilder report = new StringBuilder()
                .append('*')
                .append(entityName)
                .append('*')
                .append("\r\n");

        //Выводит справку
        for (String key : stats.keySet()) {
            //Исключения
            if (key.equals("confirmed-timeout")) {
                continue;
            }

            report
                    .append(getText("report", key))
                    .append('*')
                    .append(stats.getInt(key))
                    .append('*')
                    .append("\r\n");
        }

        //Перегородка
        final int COUNT = 25;
        report
                .append("=".repeat(COUNT))
                .append("\r\n");


        //Выводит результат
        for (String key : results.keySet()) {
            report
                    .append(getText("report", results.getJSONObject(key).getString("category")).charAt(0))
                    .append('*')
                    .append(key)
                    .append('*')
                    .append("\r\n");
        }

        return report.toString();
    }
}
