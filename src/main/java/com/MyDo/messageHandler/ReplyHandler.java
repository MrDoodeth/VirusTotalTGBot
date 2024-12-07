package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ReplyHandler implements MessageHandler {
    @Override
    public void getResponse(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (update.getCallbackQuery().getData().equals("check")) {

            boolean access = AccessChecker.check(update);

            if (access) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getMeetCondition());
            } else {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getNotMeetCondition());
            }
        }
    }
}