package com.MyDo.locker;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.tool.InlineKeyboardMarkupBuilder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/*
 * Copyright 2025 MrDoodeth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class AccessChecker {
    private static final Logger log = LoggerFactory.getLogger(AccessChecker.class);

    private static final ArrayList<Config.Chat> chatList = new ObjectMapper().convertValue(Config.getINSTANCE().getChats(), new TypeReference<>() {
    });

    private static final Map<Long, UserStatus> usersStatus = new HashMap<>();

    private static boolean subscriptionToggle = true;

    public static synchronized boolean isSubscriptionToggle() {
        return subscriptionToggle;
    }

    public static synchronized void switchSubscriptionToggle() {
        subscriptionToggle = !subscriptionToggle;
        log.info("Subscription checking: {}", subscriptionToggle);
    }

    public static UserStatus getUserStatus(Update update) {
        final User userFrom = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        final long userId = userFrom.getId();
        return usersStatus.get(userId);
    }

    private static boolean updateUserStatus(long userId, UserStatus userStatus) {
        if (usersStatus.containsKey(userId)) {
            usersStatus.replace(userId, userStatus);
            return true;
        } else {
            usersStatus.put(userId, userStatus);
            return false;
        }
    }

    public static void checkStatus(Update update) {
        final User userFrom = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        final long userId = userFrom.getId();

        UserStatus userStatus = UserStatus.ACCESS;
        UserData.addUserID(userId);

        Set<Long> adminListUsers = new ObjectMapper().convertValue(Config.getINSTANCE().getAdminIds(), new TypeReference<>() {
        });
        if (adminListUsers.contains(userId)) {
            userStatus = UserStatus.ADMINISTRATOR;
            if (updateUserStatus(userId, userStatus)) {
                return;
            }
        }

        Set<Long> blackListUsers = new ObjectMapper().convertValue(Config.getINSTANCE().getBlackList(), new TypeReference<>() {
        });
        if (blackListUsers.contains(userId)) {
            userStatus = UserStatus.BLOCKED;
            if (updateUserStatus(userId, userStatus)) {
                return;
            }
        }

        if (!subscriptionToggle) {
            updateUserStatus(userId, userStatus);
            return;
        }

        for (Config.Chat chat : chatList) {
            GetChatMember getChatMember = new GetChatMember(Long.toString(chat.getId()), userId);
            try {
                ChatMember chatMember = Bot.getINSTANCE().execute(getChatMember);
                String status = chatMember.getStatus();
                log.debug("UserID: {}, Status: {}, ChatID: {}", userFrom.getId(), status, chat.getId());
                if (status.equals("left") || status.equals("kicked")) {
                    userStatus = UserStatus.FORBIDDEN;
                    break;
                }
            } catch (TelegramApiException e) {
                log.error("Checking error: {}", e.getMessage());
            }
        }

        updateUserStatus(userId, userStatus);
    }

    public static void sendRequirementsMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(InlineKeyboardMarkupBuilder.buildAccessRequirements());

        Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getMessages().getCondition(), sendMessage);
    }
}