package com.MyDo.locker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class UserData {
    private static final Logger log = LoggerFactory.getLogger(UserData.class);

    private static final Path path = Path.of("users-id.txt");

    public static Path getPath() {
        return path;
    }

    private static final Set<Long> usersIdSet = new HashSet<>();

    public static void Init() {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    usersIdSet.add(Long.parseLong(line.trim()));
                } catch (NumberFormatException e) {
                    log.error("Can not convert line to digit: {}", line);
                }
            }
        } catch (IOException e) {
            log.error("File reading error ({}): {}", path, e.getMessage());
        }
    }

    public static void addUserID(long userId) {
        if (!usersIdSet.contains(userId)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
                writer.write(Long.toString(userId));
                writer.newLine();
                log.info("Successfully added userID: {}", userId);
            } catch (IOException e) {
                log.error("File writing error ({}): {}", path, e.getMessage());
            }
        }
        usersIdSet.add(userId);
    }
}