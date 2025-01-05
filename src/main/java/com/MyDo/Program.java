package com.MyDo;

import com.MyDo.bot.Bot;
import com.MyDo.config.Config;
import com.MyDo.locker.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

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

public class Program {
    private static final Logger log = LoggerFactory.getLogger(Program.class);

    public static void main(String[] args) throws TelegramApiException {
        final String TG_API_TOKEN = System.getenv("TG_BOT_API");
        final String TG_BOT_USERNAME = System.getenv("TG_BOT_NAME");
        final String VIRUS_TOTAL_API_TOKEN = System.getenv("VT_API");

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        Bot.setINSTANCE(new Bot(TG_API_TOKEN, TG_BOT_USERNAME, VIRUS_TOTAL_API_TOKEN));
        botsApi.registerBot(Bot.getINSTANCE());

        Config.init();
        UserData.init();

        log.info("Bot started");
    }
}