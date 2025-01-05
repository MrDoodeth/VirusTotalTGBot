package com.MyDo.uploader;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

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

public interface Uploader {
    JSONObject uploadOnVirusTotal() throws TelegramApiException, IOException, InterruptedException;
}
