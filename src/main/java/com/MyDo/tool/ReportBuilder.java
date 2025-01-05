package com.MyDo.tool;

import com.MyDo.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.Map;

/*
 * Copyright 2025 MrDoodeth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        //Reference
        for (String key : stats.keySet()) {

            //Except
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

        //Border
        report
                .append("=".repeat(COUNT))
                .append("\r\n");


        //Result
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