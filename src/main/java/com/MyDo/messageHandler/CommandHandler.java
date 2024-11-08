package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Configurator;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler extends MessageHandler {
    public CommandHandler(Bot bot) {
        super(bot);
    }

    public void getResponse(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if ("/start".equalsIgnoreCase(text)) {
            bot.sendMessage(chatId, Configurator.getText("commands", "start"));
        } else if (text.equalsIgnoreCase("/help")) {
            //return "Список доступных команд:\n/start - начать\n/help - помощь";
        } else {
            bot.sendMessage(chatId, Configurator.getText("commands", "misunderstanding-command"));
        }
    }
}