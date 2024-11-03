package com.MyDo.messageHandler;

import com.MyDo.bot.Bot;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class MessageHandler {
    protected final Bot bot;

    MessageHandler(Bot bot) {
        this.bot = bot;
    }

    public abstract void getResponse(Update update);
}