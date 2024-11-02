package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Configurator;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler extends MessageHandler {

    public CommandHandler(Bot bot) {
        super(bot);
    }

    public void getResponse(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        SendMessage sendMessage = null;
        switch (text) {
            case "/start":
                bot.sendMessage(chatId, Configurator.getVariable("messages", "start").toString());
        }
    }
}
