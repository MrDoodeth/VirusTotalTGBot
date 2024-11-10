package com.MyDo.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;

public abstract class HttpBodyBuilder {
    public static HttpRequest.BodyPublisher createMultipart(String boundary, InputStream fileInputStream, String fileName) throws IOException {
        var byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] fileBytes = fileInputStream.readAllBytes();

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
