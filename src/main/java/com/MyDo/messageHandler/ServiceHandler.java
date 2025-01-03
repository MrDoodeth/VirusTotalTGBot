package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.tool.Analyzer;
import com.MyDo.tool.ReportBuilder;
import com.MyDo.tool.UrlValidator;
import com.MyDo.uploader.FileUploader;
import com.MyDo.uploader.Uploader;
import com.MyDo.uploader.UrlUploader;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;

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

public class ServiceHandler implements MessageHandler {

    private static final int MAX_FILE_SIZE = 20971520; //20 MB

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

            if (document.getFileSize() <= MAX_FILE_SIZE) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getWaiting());
            } else {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getTooLargeFile());
                return;
            }
        } else if (UrlValidator.isValidUrl(text)) {
            uploader = new UrlUploader(text);
            entityName = text;
            Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getWaiting());
        } else {
            Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getMisunderstanding());
            return;
        }

        JSONObject scanningResult = Analyzer.getVirusTotalReport(uploader);

        final String report;
        if (scanningResult != null)
            report = ReportBuilder.build(entityName, scanningResult);
        else
            report = Config.getINSTANCE().getMessages().getError();

        SendMessage sendMessage = new SendMessage();
        sendMessage.disableWebPagePreview();
        Bot.getINSTANCE().sendMessage(chatId, report, sendMessage);
    }
}