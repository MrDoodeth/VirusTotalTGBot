package com.MyDo.uploader;

import com.MyDo.bot.Bot;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static com.MyDo.tool.HttpBodyBuilder.createMultipart;

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

public class FileUploader implements Uploader {
    private static final Logger log = LoggerFactory.getLogger(FileUploader.class);

    private final String fileName;
    private final String fileId;

    public FileUploader(String fileName, String fileId) {
        this.fileName = fileName;
        this.fileId = fileId;
    }

    @Override
    public JSONObject uploadOnVirusTotal() throws TelegramApiException, IOException, InterruptedException {

        final HttpClient client = HttpClient.newHttpClient();
        final String boundary = UUID.randomUUID().toString();
        final String fileUrl = getFileTGUrl();
        final InputStream fileInputStream = new URL(fileUrl).openStream();

        final HttpRequest requestToPostFile = HttpRequest.newBuilder()
                .uri(URI.create("https://www.virustotal.com/api/v3/files"))
                .header("accept", "application/json")
                .header("x-apikey", Bot.getINSTANCE().getVirusTotalApiToken())
                .header("content-type", "multipart/form-data; boundary=" + boundary)
                .POST(createMultipart(boundary, fileInputStream, fileName))
                .build();

        log.info("Uploading file to VirusTotal");
        HttpResponse<String> uploadFileResponse = client.send(requestToPostFile, HttpResponse.BodyHandlers.ofString());
        log.info("Uploading completed");
        return new JSONObject(uploadFileResponse.body());
    }

    private String getFileTGUrl() throws TelegramApiException {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        org.telegram.telegrambots.meta.api.objects.File file = Bot.getINSTANCE().execute(getFileRequest);
        return "https://api.telegram.org/file/bot" + Bot.getINSTANCE().getBotToken() + "/" + file.getFilePath();
    }
}