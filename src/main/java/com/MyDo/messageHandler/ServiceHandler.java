package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Configurator;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ServiceHandler extends MessageHandler {
    public ServiceHandler(Bot bot) {
        super(bot);
    }

    public void getResponse(Update update) {
        if (update.getMessage().hasDocument()) {
            String fileName = update.getMessage().getDocument().getFileName();
            String fileId = update.getMessage().getDocument().getFileId();
            long chatId = update.getMessage().getChatId();

            bot.sendMessage(chatId, Configurator.getVariable("messages", "waiting").toString());
            saveFile(fileName, fileId);
        }
    }

    private void saveFile(String fileName, String fileId) {

        GetFile getFileRequest = new GetFile();
        getFileRequest.setFileId(fileId);
        org.telegram.telegrambots.meta.api.objects.File file;

        try {
            file = bot.execute(getFileRequest);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        String fileUrl = "https://api.telegram.org/file/bot" + bot.getBotToken() + "/" + file.getFilePath();
        String savePath = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "uploadedFiles" + File.separator + fileName;

        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            System.out.println("Файл сохранен: " + savePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
