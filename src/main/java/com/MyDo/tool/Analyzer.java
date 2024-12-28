package com.MyDo.tool;

import com.MyDo.bot.Bot;
import com.MyDo.uploader.Uploader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Analyzer {
    private static final Logger log = LoggerFactory.getLogger(Analyzer.class);

    private static final int COUNT_OF_REPEAT = 20; // Проверка MAX = 5 min
    private static final int WAIT_TIME = 15_000; //Ограничение API = 4 lookups / min

    public static JSONObject getVirusTotalReport(Uploader uploader) {
        final HttpClient client = HttpClient.newHttpClient();
        JSONObject analysisResult = null;

        String selfLink = "";
        try {
            JSONObject uploadResult = uploader.uploadOnVirusTotal();

            String analysisId = uploadResult.getJSONObject("data").getString("id");

            URI analysisUri = URI.create("https://www.virustotal.com/api/v3/analyses/" + analysisId);
            HttpRequest analysisRequest = HttpRequest.newBuilder()
                    .uri(analysisUri)
                    .header("x-apikey", Bot.getINSTANCE().getVirusTotalApiToken())
                    .GET()
                    .build();

            for (int i = 0; i < COUNT_OF_REPEAT; i++) {

                //Ждём, пока завершится анализ
                log.debug("Try №{}", i+1);
                Thread.sleep(WAIT_TIME);

                HttpResponse<String> analysisResponse = client.send(analysisRequest, HttpResponse.BodyHandlers.ofString());
                analysisResult = new JSONObject(analysisResponse.body());

                String status = analysisResult.getJSONObject("data").getJSONObject("attributes").getString("status");
                selfLink = analysisResult.getJSONObject("data").getJSONObject("links").getString("self");
                log.info("Analyze status: {}", status);

                if (status.equals("completed")) {
                    break;
                } else {
                    analysisResult = null;
                }
            }

        } catch (IOException | InterruptedException | TelegramApiException e) {
            log.error(e.getMessage());
        }
        log.info("Sent report ({})", selfLink);
        return analysisResult;
    }
}