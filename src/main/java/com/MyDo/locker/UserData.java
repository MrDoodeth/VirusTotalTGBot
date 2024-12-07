package com.MyDo.locker;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class UserData {
    private static final Path path = Path.of("src", "main", "resources", "users-id.txt");
    private static final Set<Long> usersIdSet = new HashSet<>();

    public static void Init() {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    usersIdSet.add(Long.parseLong(line.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Невозможно преобразовать строку в число: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    public static void addUserID(long userId) {
        if (!usersIdSet.contains(userId)) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile(), true))) {
                writer.println(userId);
                System.out.println("Файл записан успешно.");
            } catch (IOException e) {
                System.err.println("Ошибка записи файла: " + e.getMessage());
            }
        }
        usersIdSet.add(userId);
    }
}