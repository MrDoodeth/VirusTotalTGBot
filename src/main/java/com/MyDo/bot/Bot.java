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
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.nio.file.Path;


public class Bot extends TelegramLongPollingBot {
    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private static Bot INSTANCE;

    private final String apiBotToken;
    private final String botUsername;
    private final String virusTotalApiToken;

    public Bot(String apiBotToken, String botUsername, String virusTotalApiToken) {
        this.apiBotToken = apiBotToken;
        this.botUsername = botUsername;
        this.virusTotalApiToken = virusTotalApiToken;
    }

    public static synchronized Bot getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(Bot INSTANCE) {
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

    @Override
    public void onUpdateReceived(Update update) {
        MessageHandler messageHandler = null;

        if (update.hasCallbackQuery()) {
            messageHandler = new ReplyHandler();
        }

        if (update.hasMessage()) {
            //Проверка на доступ к боту
           AccessChecker.checkStatus(update);
           UserStatus userStatus = AccessChecker.getUserStatus(update);

            String text = update.getMessage().getText();
            final long userChatId = update.getMessage().getChatId();

            switch (userStatus) {
                case FORBIDDEN:
                    AccessChecker.sendRequirementsMessage(userChatId);
                    break;
                case BLOCKED:
                    Bot.getINSTANCE().sendMessage(userChatId, Config.getINSTANCE().getMessages().getBlackList());
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

    public void sendMessage(long chatId, String textMessage) {
        sendMessage(chatId, textMessage, new SendMessage());
    }

    public void sendMessage(long chatId, String textMessage, SendMessage sendMessage) {
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.enableMarkdown(true);

        //Ограничение TG на максимальную длинну
        final int MAX_LENGTH = 4096;
        textMessage = textMessage.length() > MAX_LENGTH ? textMessage.substring(0, MAX_LENGTH) : textMessage;
        sendMessage.setText(textMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Sending message error: {}", e.getMessage());
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