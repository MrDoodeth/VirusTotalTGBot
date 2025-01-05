package com.MyDo.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

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

public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);

    private static final Path path = Path.of("config.json");

    public static void init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Config config = mapper.readValue(path.toFile(), Config.class);
            Config.setINSTANCE(config);
        } catch (IOException e) {
            log.error("Initialize config error ({}): {}", path, e.getMessage());
        }
    }

    private static Config INSTANCE;

    public static Config getINSTANCE() {
        return INSTANCE;
    }

    public static void setINSTANCE(Config INSTANCE) {
        if (Config.INSTANCE == null) {
            Config.INSTANCE = INSTANCE;
        }
    }

    @JsonProperty("messages")
    private Messages messages;

    @JsonProperty("commands")
    private Commands commands;

    @JsonProperty("report")
    private Report report;

    @JsonProperty("chats")
    private List<Chat> chats;

    @JsonProperty("admin-id")
    private List<Long> adminIds;

    @JsonProperty("black-list")
    private List<Long> blackList;

    public Messages getMessages() {
        return messages;
    }

    public Commands getCommands() {
        return commands;
    }

    public Report getReport() {
        return report;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public List<Long> getAdminIds() {
        return adminIds;
    }

    public List<Long> getBlackList() {
        return blackList;
    }

    public static class Messages {

        @JsonProperty("waiting")
        private String waiting;

        @JsonProperty("misunderstanding")
        private String misunderstanding;

        @JsonProperty("too-large-file")
        private String tooLargeFile;

        @JsonProperty("error")
        private String error;

        @JsonProperty("condition")
        private String condition;

        @JsonProperty("chanel")
        private String chanel;

        @JsonProperty("black-list")
        private String blackList;

        public String getWaiting() {
            return waiting;
        }

        public String getMisunderstanding() {
            return misunderstanding;
        }

        public String getTooLargeFile() {
            return tooLargeFile;
        }

        public String getError() {
            return error;
        }

        public String getCondition() {
            return condition;
        }

        public String getChanel() {
            return chanel;
        }

        public String getBlackList() {
            return blackList;
        }
    }

    public static class Commands {
        @JsonProperty("start")
        private String start;

        @JsonProperty("misunderstanding")
        private String misunderstanding;

        @JsonProperty("help")
        private String help;

        @JsonProperty("admin")
        private String admin;

        @JsonProperty("not-admin")
        private String notAdmin;

        @JsonProperty("users-id")
        private String usersId;

        @JsonProperty("subscription-check-toggle")
        private String subscriptionToggle;

        @JsonProperty("subscription-check-on")
        private String subscriptionOn;

        @JsonProperty("subscription-check-off")
        private String subscriptionOff;

        public String getStart() {
            return start;
        }

        public String getMisunderstanding() {
            return misunderstanding;
        }

        public String getHelp() {
            return help;
        }

        public String getAdmin() {
            return admin;
        }

        public String getNotAdmin() {
            return notAdmin;
        }

        public String getUsersId() {
            return usersId;
        }

        public String getSubscriptionToggle() {
            return subscriptionToggle;
        }

        public String getSubscriptionOn() {
            return subscriptionOn;
        }

        public String getSubscriptionOff() {
            return subscriptionOff;
        }
    }

    public static class Report {
        @JsonProperty("malicious")
        private String malicious;

        @JsonProperty("type-unsupported")
        private String typeUnsupported;

        @JsonProperty("failure")
        private String failure;

        @JsonProperty("undetected")
        private String undetected;

        @JsonProperty("suspicious")
        private String suspicious;

        @JsonProperty("harmless")
        private String harmless;

        @JsonProperty("timeout")
        private String timeout;
    }

    public static class Chat {
        @JsonProperty("url")
        private String url;

        @JsonProperty("id")
        private long id;

        public String getUrl() {
            return url;
        }

        public long getId() {
            return id;
        }
    }
}