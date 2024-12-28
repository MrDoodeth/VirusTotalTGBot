package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import com.MyDo.locker.UserData;
import com.MyDo.locker.UserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ReplyHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(ReplyHandler.class);

    @Override
    public void getResponse(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (update.getCallbackQuery().getData().equals("check")) {

            UserStatus userStatus = AccessChecker.getUserStatus(update);

            if (userStatus == UserStatus.ACCESS) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getMeetCondition());
            } else if (userStatus == UserStatus.FORBIDDEN) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getNotMeetCondition());
            }
        } else if (update.getCallbackQuery().getData().equals("get-users-id")) {
            Bot.getINSTANCE().sendDocument(chatId, UserData.getPath());
            log.info("Sent {}", UserData.getPath());
        }
    }
}