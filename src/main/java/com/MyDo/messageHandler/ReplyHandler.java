package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import com.MyDo.locker.UserStatus;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.nio.file.Path;

public class ReplyHandler implements MessageHandler {
    private static final Path usersIdFilePath = Path.of("src", "main", "resources", "users-id.txt");
    @Override
    public void getResponse(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        if (update.getCallbackQuery().getData().equals("check")) {

            UserStatus userStatus = AccessChecker.checkStatus(update);

            if (userStatus == UserStatus.ACCESS) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getMeetCondition());
            } else if (userStatus == UserStatus.FORBIDDEN) {
                Bot.getINSTANCE().sendMessage(chatId, Config.getINSTANCE().getMessages().getNotMeetCondition());
            }
        } else if (update.getCallbackQuery().getData().equals("get-users-id")) {
            Bot.getINSTANCE().sendDocument(chatId, usersIdFilePath);
        }
    }
}