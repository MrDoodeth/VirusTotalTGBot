package com.MyDo.tool;

import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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

public class InlineKeyboardMarkupBuilder {
    private static final ArrayList<Config.Chat> chatList = new ObjectMapper().convertValue(Config.getINSTANCE().getChats(), new TypeReference<>() {
    });

    public static InlineKeyboardMarkup buildAccessRequirements() {
        InlineKeyboardButton[] linkButtons = new InlineKeyboardButton[4];

        for (int i = 0; i < linkButtons.length; i++) {
            linkButtons[i] = InlineKeyboardButton.builder()
                    .url(chatList.get(i).getUrl())
                    .text(Config.getINSTANCE().getMessages().getChanel() + ' ' + "№" + (i + 1))
                    .callbackData("button" + (i + 1))
                    .build();
        }

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(linkButtons[0], linkButtons[1]))
                .keyboardRow(List.of(linkButtons[2], linkButtons[3]))
                .build();

        return keyboard;
    }

    public static InlineKeyboardMarkup buildAdminPanel() {
        InlineKeyboardButton usersIdButton;
        InlineKeyboardButton subscriptionCheckToggleButton;

        usersIdButton = InlineKeyboardButton.builder()
                .text(Config.getINSTANCE().getCommands().getUsersId())
                .callbackData("get-users-id")
                .build();

        String subscriptionStatus;
        if (AccessChecker.isSubscriptionToggle()) {
            subscriptionStatus = Config.getINSTANCE().getCommands().getSubscriptionOn();
        } else {
            subscriptionStatus = Config.getINSTANCE().getCommands().getSubscriptionOff();
        }

        subscriptionCheckToggleButton = InlineKeyboardButton.builder()
                .text(Config.getINSTANCE().getCommands().getSubscriptionToggle() + subscriptionStatus)
                .callbackData("switch-subscription")
                .build();

        InlineKeyboardMarkup keyboard = InlineKeyboardMarkup.builder()
                .keyboardRow(List.of(usersIdButton))
                .keyboardRow(List.of(subscriptionCheckToggleButton))
                .build();

        return keyboard;
    }
}
