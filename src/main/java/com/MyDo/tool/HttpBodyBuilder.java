package com.MyDo.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

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

public class HttpBodyBuilder {
    public static HttpRequest.BodyPublisher createMultipart(String boundary, InputStream fileInputStream, String fileName) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] fileBytes = fileInputStream.readAllBytes();

        byteArrayOutputStream.write(("--" + boundary + "\r\n").getBytes());
        byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n").getBytes());
        byteArrayOutputStream.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());
        byteArrayOutputStream.write(fileBytes);
        byteArrayOutputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

        return HttpRequest.BodyPublishers.ofByteArray(byteArrayOutputStream.toByteArray());
    }

    public static HttpRequest.BodyPublisher createWWW(String url) {
        String body = "url=" + URLEncoder.encode(url, StandardCharsets.UTF_8);
        return HttpRequest.BodyPublishers.ofString(body);
    }
}