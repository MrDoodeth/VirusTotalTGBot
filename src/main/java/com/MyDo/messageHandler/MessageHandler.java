package com.MyDo.messageHandler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void getResponse(Update update);
}