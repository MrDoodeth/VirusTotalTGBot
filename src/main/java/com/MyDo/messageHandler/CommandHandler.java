package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.locker.AccessChecker;
import com.MyDo.locker.UserStatus;
import com.MyDo.tool.InlineKeyboardMarkupBuilder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

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

public class CommandHandler implements MessageHandler {

    @Override
    public void getResponse(Update update) {
        String text = update.getMessage().getText();

        if (text.equalsIgnoreCase("/start")) {
            Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getCommands().getStart());
        } else if (text.equalsIgnoreCase("/help")) {
            Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getCommands().getHelp());
        } else if (text.equalsIgnoreCase("/admin")) {
            UserStatus userStatus = AccessChecker.getUserStatus(update);

            if (userStatus != UserStatus.ADMINISTRATOR) {
                Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getCommands().getNotAdmin());
                return;
            }

            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(InlineKeyboardMarkupBuilder.buildAdminPanel());

            Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getCommands().getAdmin(), sendMessage);
        } else {
            Bot.getINSTANCE().sendMessage(update, Config.getINSTANCE().getCommands().getMisunderstanding());
        }
    }
}