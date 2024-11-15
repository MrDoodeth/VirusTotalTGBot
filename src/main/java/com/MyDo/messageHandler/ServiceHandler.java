package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.tool.Analyzer;
import com.MyDo.tool.ReportBuilder;
import com.MyDo.tool.UrlValidator;
import com.MyDo.uploader.FileUploader;
import com.MyDo.uploader.Uploader;
import com.MyDo.uploader.UrlUploader;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.MyDo.text.Configurator.getText;

public class ServiceHandler implements MessageHandler {

    @Override
    public void getResponse(Update update) {
        final long chatId = update.getMessage().getChatId();
        final String text = update.getMessage().getText();
        final String entityName;
        final Uploader uploader;

        if (update.getMessage().hasDocument()) {
            final Document document = update.getMessage().getDocument();
            final String fileName = document.getFileName();
            final String fileId = document.getFileId();

            uploader = new FileUploader(fileName, fileId);
            entityName = fileName;

            final int MAX_FILE_SIZE = 20971520; //20 MB

            if (document.getFileSize() <= MAX_FILE_SIZE) {
                Bot.getINSTANCE().sendMessage(chatId, getText("messages", "waiting"));
            } else {
                Bot.getINSTANCE().sendMessage(chatId, getText("messages", "too-large-file"));
                return;
            }
        } else if (UrlValidator.isValidUrl(text)) {
            uploader = new UrlUploader(text);
            entityName = text;
            Bot.getINSTANCE().sendMessage(chatId, getText("messages", "waiting"));
        } else {
            Bot.getINSTANCE().sendMessage(chatId, getText("messages", "misunderstanding-text"));
            return;
        }

        JSONObject scanningResult = Analyzer.getVirusTotalReport(uploader);

        final String report;
        if (scanningResult != null)
            report = ReportBuilder.build(entityName, scanningResult);
        else
            report = getText("messages", "error");

        Bot.getINSTANCE().sendMessage(chatId, report);
    }
}