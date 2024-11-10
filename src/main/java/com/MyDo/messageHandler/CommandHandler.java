package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.text.Configurator;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler implements MessageHandler {

    @Override
    public void getResponse(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if ("/start".equalsIgnoreCase(text)) {
            Bot.getINSTANCE().sendMessage(chatId, Configurator.getText("commands", "start"));
        } else if (text.equalsIgnoreCase("/help")) {
            //return "Список доступных команд:\n/start - начать\n/help - помощь";
        } else {
            Bot.getINSTANCE().sendMessage(chatId, Configurator.getText("commands", "misunderstanding-command"));
        }
    }
}