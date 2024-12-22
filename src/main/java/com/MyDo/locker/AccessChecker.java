package com.MyDo.locker;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AccessChecker {
    private static final Logger log = LoggerFactory.getLogger(AccessChecker.class);

    private static final ArrayList<Config.Chat> chatList = new ObjectMapper().convertValue(Config.getINSTANCE().getChats(), new TypeReference<>() {
    });

    public static UserStatus checkStatus(Update update) {

        UserStatus userStatus = UserStatus.ACCESS;

        final User userFrom = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        final long userId = userFrom.getId();

        UserData.addUserID(userId);

        Set<Long> adminListUsers = new ObjectMapper().convertValue(Config.getINSTANCE().getAdminIds(), new TypeReference<>() {});
        if (adminListUsers.contains(userId)) {
            userStatus = UserStatus.ADMINISTRATOR;
            return userStatus;
        }

        Set<Long> blackListUsers = new ObjectMapper().convertValue(Config.getINSTANCE().getBlackList(), new TypeReference<>() {});
        if (blackListUsers.contains(userId)) {
            userStatus = UserStatus.BLOCKED;
            return userStatus;
        }

        //Проверка на присутствие в каналах
        for (Config.Chat chat : chatList) {
            GetChatMember getChatMember = new GetChatMember(Long.toString(chat.getId()), userId);
            try {
                ChatMember chatMember = Bot.getINSTANCE().execute(getChatMember);
                String status = chatMember.getStatus();
                log.debug("UserName: {}, Status: {}", userFrom.getUserName(), status);
                if (status.equals("left") || status.equals("kicked")) {
                    userStatus = UserStatus.FORBIDDEN;
                    break;
                }
            } catch (TelegramApiException e) {
                log.error("Checking error: {}", e.getMessage());
            }
        }

        return userStatus;
    }

    public static void sendRequirementsMessage(long userChatId) {
        SendMessage sendMessage = new SendMessage();
        InlineKeyboardButton[] linkButtons = new InlineKeyboardButton[4];
        InlineKeyboardButton checkButton;

        for (int i = 0; i < linkButtons.length; i++) {
            linkButtons[i] = InlineKeyboardButton.builder()
                    .url(chatList.get(i).getUrl())
                    .text(Config.getINSTANCE().getMessages().getChanel() + ' ' +  "№" + (i + 1))
                    .callbackData("button" + (i + 1))
                    .build();
        }

        checkButton = InlineKeyboardButton.builder()
                .text(Config.getINSTANCE().getMessages().getCheck())
                .callbackData("check")
                .build();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(linkButtons[0], linkButtons[1]))
                .keyboardRow(List.of(linkButtons[2], linkButtons[3]))
                .keyboardRow(List.of(checkButton))
                .build();

        sendMessage.setReplyMarkup(keyboard);

        Bot.getINSTANCE().sendMessage(userChatId, Config.getINSTANCE().getMessages().getCondition(), sendMessage);
    }
}