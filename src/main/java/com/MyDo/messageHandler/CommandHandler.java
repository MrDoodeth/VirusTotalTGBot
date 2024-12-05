package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import org.telegram.telegrambots.meta.api.objects.Update;

public class CommandHandler implements MessageHandler {

    @Override
    public void getResponse(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        if ("/start".equalsIgnoreCase(text)) {
            Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getCommands().getStart());
        } else if (text.equalsIgnoreCase("/help")) {
            Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getCommands().getHelp());
        } else {
            Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getCommands().getMisunderstanding());
        }
    }
}