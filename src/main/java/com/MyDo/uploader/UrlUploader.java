package com.MyDo.uploader;

import com.MyDo.bot.Bot;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.MyDo.tool.HttpBodyBuilder.createWWW;

public class UrlUploader implements Uploader {
    private static final Logger log = LoggerFactory.getLogger(UrlUploader.class);

    private final String suspiciousUrl;

    public UrlUploader(String suspiciousUrl) {
        this.suspiciousUrl = suspiciousUrl;
    }

    @Override
    public JSONObject uploadOnVirusTotal() throws IOException, InterruptedException {
        final HttpClient client = HttpClient.newHttpClient();

        HttpRequest requestToPostUrl = HttpRequest.newBuilder()
                .uri(URI.create("https://www.virustotal.com/api/v3/urls"))
                .header("x-apikey", Bot.getINSTANCE().getVirusTotalApiToken())
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(createWWW(suspiciousUrl))
                .build();

        System.out.println(requestToPostUrl.toString());

        log.info("Uploading link to VirusTotal");
        HttpResponse<String> uploadUrlResponse = client.send(requestToPostUrl, HttpResponse.BodyHandlers.ofString());
        log.info("Uploading completed");
        return new JSONObject(uploadUrlResponse.body());
    }
}