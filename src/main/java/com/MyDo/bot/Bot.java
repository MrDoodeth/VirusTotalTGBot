package com.MyDo.bot;

import com.MyDo.messageHandler.CommandHandler;
import com.MyDo.messageHandler.MessageHandler;
import com.MyDo.messageHandler.ServiceHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {
    private final String apiBotToken;
    private final String botUsername;
    private final String virusTotalApiToken;

    public Bot(String apiBotToken, String botUsername, String virusTotalApiToken) {
        this.apiBotToken = apiBotToken;
        this.botUsername = botUsername;
        this.virusTotalApiToken = virusTotalApiToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return apiBotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        MessageHandler messageHandler;

        if (update.getMessage().getText() != null && update.getMessage().getText().charAt(0) == '/') {
            messageHandler = new CommandHandler(this);
        } else {
            messageHandler = new ServiceHandler(this);
        }

        messageHandler.getResponse(update);
    }

    public void sendMessage(long chatId, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.setText(textMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}