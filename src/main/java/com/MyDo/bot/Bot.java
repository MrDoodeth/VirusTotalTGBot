package com.MyDo.bot;

import com.MyDo.messageHandler.CommandHandler;
import com.MyDo.messageHandler.MessageHandler;
import com.MyDo.messageHandler.ServiceHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

    private static Bot INSTANCE;

    public static Bot getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(Bot INSTANCE) {
        if (Bot.INSTANCE == null) {
            Bot.INSTANCE = INSTANCE;
        }
    }

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

    public String getVirusTotalApiToken() {
        return virusTotalApiToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();

            MessageHandler messageHandler;

            if (text != null && text.charAt(0) == '/') {
                messageHandler = new CommandHandler();
            } else {
                messageHandler = new ServiceHandler();
            }

            messageHandler.getResponse(update);
        }
    }

    public Message sendMessage(long chatId, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(Long.toString(chatId));
        sendMessage.enableMarkdown(true);

        //Ограничение TG на максимальную длинну
        final int MAX_LENGTH = 4096;
        textMessage = textMessage.length() > MAX_LENGTH ? textMessage.substring(0, MAX_LENGTH) : textMessage;
        sendMessage.setText(textMessage);

        try {
            return execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}