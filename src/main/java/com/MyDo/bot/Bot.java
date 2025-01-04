package com.MyDo.bot;

import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import com.MyDo.locker.UserStatus;
import com.MyDo.messageHandler.CommandHandler;
import com.MyDo.messageHandler.MessageHandler;
import com.MyDo.messageHandler.ReplyHandler;
import com.MyDo.messageHandler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Path;
import java.util.HashMap;
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

public class Bot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private static Bot INSTANCE;

    private final String apiBotToken;
    private final String botUsername;
    private final String virusTotalApiToken;

    private static Map<Long, Message> lastBotMessages = new HashMap<>();

    public Bot(String apiBotToken, String botUsername, String virusTotalApiToken) {
        this.apiBotToken = apiBotToken;
        this.botUsername = botUsername;
        this.virusTotalApiToken = virusTotalApiToken;
    }

    public static synchronized Bot getINSTANCE() {
        return INSTANCE;
    }

    public static synchronized void setINSTANCE(Bot INSTANCE) {
        if (Bot.INSTANCE == null) {
            Bot.INSTANCE = INSTANCE;
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return apiBotToken;
    }

    public String getVirusTotalApiToken() {
        return virusTotalApiToken;
    }

    public static synchronized Message getLastBotMessage(Update update) {
        final User userFrom = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        final long userId = userFrom.getId();
        return lastBotMessages.get(userId);
    }

    private static synchronized void updateLastBotMessage(Update update, Message message) {
        final User userFrom = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        final long userId = userFrom.getId();

        if (lastBotMessages.containsKey(userId)) {
            lastBotMessages.replace(userId, message);
        } else {
            lastBotMessages.put(userId, message);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        MessageHandler messageHandler = null;

        if (update.hasCallbackQuery()) {
            messageHandler = new ReplyHandler();
        }

        if (update.hasMessage()) {

            AccessChecker.checkStatus(update);
            UserStatus userStatus = AccessChecker.getUserStatus(update);

            String text = update.getMessage().getText();

            switch (userStatus) {
                case FORBIDDEN:
                    AccessChecker.sendRequirementsMessage(update);
                    break;
                case BLOCKED:
                    Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getMessages().getBlackList());
                    break;
                default:
                    if (text != null && update.getMessage().isCommand()) {
                        messageHandler = new CommandHandler();
                    } else {
                        messageHandler = new ServiceHandler();
                    }
                    break;
            }
        }

        if (messageHandler != null) {
            messageHandler.getResponse(update);
        }
    }

    public void sendMessage(Update update, String textMessage) {
        sendMessage(update, textMessage, new SendMessage());
    }

    public void sendMessage(Update update, String textMessage, SendMessage sendMessage) {
        String chatId = update.hasMessage() ? Long.toString(update.getMessage().getChatId()) : Long.toString(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);

        //Telegram limit for max length
        final int MAX_LENGTH = 4096;
        textMessage = textMessage.length() > MAX_LENGTH ? textMessage.substring(0, MAX_LENGTH) : textMessage;
        sendMessage.setText(textMessage);

        try {
            updateLastBotMessage(update, execute(sendMessage));
        } catch (TelegramApiException e) {
            log.error("Sending message error: {}", e.getMessage());
        }
    }

    public void editLastMessageReplyMarkup(Update update, InlineKeyboardMarkup newInlineKeyboardMarkup) {
        String chatId = Long.toString(getLastBotMessage(update).getChatId());
        int messageId = getLastBotMessage(update).getMessageId();

        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(newInlineKeyboardMarkup);

        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error("Editing message error: {}", e.getMessage());
        }
    }

    public void sendDocument(long chatId, Path filePath) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(Long.toString(chatId));
        sendDocument.setDocument(new InputFile(filePath.toFile()));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("Sending document error: {}", e.getMessage());
        }
    }
}