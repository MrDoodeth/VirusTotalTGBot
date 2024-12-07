package com.MyDo.locker;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class AccessChecker {
    private static final ArrayList<Config.Chat> chatList = new ObjectMapper().convertValue(Config.getINSTANCE().getChats(), new TypeReference<>() {
    });

    public static boolean check(Update update) {

        boolean access = true;
        final long userId = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom().getId() : update.getMessage().getFrom().getId();

        UserData.addUserID(userId);

        //Проверка на присутствие в каналах
        for (Config.Chat chat : chatList) {
            GetChatMember getChatMember = new GetChatMember(Long.toString(chat.getId()), userId);
            try {
                ChatMember chatMember = Bot.getINSTANCE().execute(getChatMember);
                String status = chatMember.getStatus();
                System.out.println(status);
                if (status.equals("left") || status.equals("kicked")) {
                    access = false;
                    break;
                }
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }

        return access;
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