package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Configurator;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServiceHandler extends MessageHandler {

    public ServiceHandler(Bot bot) {
        super(bot);
    }

    public void getResponse(Update update) {
        Document document = update.getMessage().getDocument();
        String fileName = document.getFileName();
        String fileId = document.getFileId();
        long chatId = update.getMessage().getChatId();

        if (update.getMessage().hasDocument()) {
            final int MAX_FILE_SIZE = 20971520; //20 MB

            if (document.getFileSize() <= MAX_FILE_SIZE) {
                bot.sendMessage(chatId, Configurator.getText("messages", "waiting"));
            } else {
                bot.sendMessage(chatId, Configurator.getText("messages", "too-large-file"));
                return;
            }

            String scanningResult = getVirusTotalReport(fileName, fileId).toString();

            //Логика обработки отчёта

            bot.sendMessage(chatId, scanningResult);
        }
    }

    private JSONObject getVirusTotalReport(String fileName, String fileId) {
        HttpClient client = HttpClient.newHttpClient();
        JSONObject analysisResult = null;

        try {
            JSONObject uploadResult = uploadFileOnVirusTotal(client, fileId, fileName);

            String analysisId = uploadResult.getJSONObject("data").getString("id");

            URI analysisUri = URI.create("https://www.virustotal.com/api/v3/analyses/" + analysisId);
            HttpRequest analysisRequest = HttpRequest.newBuilder()
                    .uri(analysisUri)
                    .header("accept", "application/json")
                    .header("x-apikey", bot.getVirusTotalApiToken())
                    .GET()
                    .build();

            for (int i = 0; i < 10; i++) {
                System.out.println("Попытка №" + (i + 1));
                HttpResponse<String> analysisResponse = client.send(analysisRequest, HttpResponse.BodyHandlers.ofString());
                analysisResult = new JSONObject(analysisResponse.body());

                if (analysisResult != null) {
                    break;
                }

                TimeUnit.SECONDS.sleep(6);
            }

        } catch (IOException | InterruptedException | TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Отсылаю ответ");
        System.out.println(analysisResult);
        return analysisResult;
    }

    private JSONObject uploadFileOnVirusTotal(HttpClient client, String fileId, String fileName) throws TelegramApiException, IOException, InterruptedException {

        String uploadUrl = "https://www.virustotal.com/api/v3/files";
        String boundary = UUID.randomUUID().toString();
        String fileUrl = getFileTGUrl(fileId);
        InputStream fileInputStream = new URL(fileUrl).openStream();

        HttpRequest requestToPostFile = HttpRequest.newBuilder()
                .uri(URI.create(uploadUrl))
                .header("accept", "application/json")
                .header("x-apikey", bot.getVirusTotalApiToken())
                .header("content-type", "multipart/form-data; boundary=" + boundary)
                .POST(createMultipartBody(boundary, fileInputStream, fileName))
                .build();

        HttpResponse<String> uploadFileResponse = client.send(requestToPostFile, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(uploadFileResponse.body());
    }

    private String getFileTGUrl(String fileId) throws TelegramApiException {
        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);

        org.telegram.telegrambots.meta.api.objects.File file = bot.execute(getFileRequest);

        return "https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + file.getFilePath();
    }

    // Создаёт тело запроса для отправки файла
    public HttpRequest.BodyPublisher createMultipartBody(String boundary, InputStream fileInputStream, String fileName) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] fileBytes = fileInputStream.readAllBytes();

        byteArrayOutputStream.write(("--" + boundary + "\r\n").getBytes());
        byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes());
        byteArrayOutputStream.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());
        byteArrayOutputStream.write(fileBytes);
        byteArrayOutputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        return HttpRequest.BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray());
    }
}