package com.MyDo.tool;

import com.MyDo.bot.Bot;
import com.MyDo.uploader.Uploader;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class Analyzer {

    public static JSONObject getVirusTotalReport(Uploader uploader) {
        final HttpClient client = HttpClient.newHttpClient();
        JSONObject analysisResult = null;

        try {
            JSONObject uploadResult = uploader.uploadOnVirusTotal();

            String analysisId = uploadResult.getJSONObject("data").getString("id");

            URI analysisUri = URI.create("https://www.virustotal.com/api/v3/analyses/" + analysisId);
            HttpRequest analysisRequest = HttpRequest.newBuilder()
                    .uri(analysisUri)
                    .header("x-apikey", Bot.getINSTANCE().getVirusTotalApiToken())
                    .GET()
                    .build();

            final int COUNT_OF_REPEAT = 20; // Проверка MAX = 5 min
            final int WAIT_TIME = 15_000; //Ограничение API = 4 lookups / min

            for (int i = 0; i < COUNT_OF_REPEAT; i++) {
                System.out.println("Попытка №" + (i + 1));
                //Ждём, пока завершится анализ
                Thread.sleep(WAIT_TIME);

                HttpResponse<String> analysisResponse = client.send(analysisRequest, HttpResponse.BodyHandlers.ofString());
                analysisResult = new JSONObject(analysisResponse.body());

                String status = analysisResult.getJSONObject("data").getJSONObject("attributes").getString("status");
                System.out.println(status);

                if (status.equals("completed")) {
                    break;
                } else {
                    analysisResult = null;
                }
            }

        } catch (IOException | InterruptedException | TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Отсылаю ответ");
        System.out.println(analysisResult);
        return analysisResult;
    }
}