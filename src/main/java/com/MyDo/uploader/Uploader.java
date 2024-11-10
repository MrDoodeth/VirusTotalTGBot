package com.MyDo.uploader;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface Uploader {
    JSONObject uploadOnVirusTotal() throws TelegramApiException, IOException, InterruptedException ;
}
