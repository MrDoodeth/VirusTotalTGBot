package com.MyDo.config;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class Config {
    private static final String path = "src/main/resources/config.json";

    public static void Init() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Config config = mapper.readValue(new File(path), Config.class);
            Config.setINSTANCE(config);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
    private List<Integer> adminIds;

    @JsonProperty("black-list")
    private List<Integer> blackList;

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

    public List<Integer> getAdminIds() {
        return adminIds;
    }

    public List<Integer> getBlackList() {
        return blackList;
    }

    public static class Messages {
        @JsonProperty("fail")
        private String fail;

        @JsonProperty("waiting")
        private String waiting;

        @JsonProperty("misunderstanding")
        private String misunderstandingText;

        @JsonProperty("too-large-file")
        private String tooLargeFile;

        @JsonProperty("error")
        private String error;

        @JsonProperty("condition")
        private String condition;

        @JsonProperty("meet-condition")
        private String meetCondition;

        @JsonProperty("black-list")
        private String blackList;

        public String getFail() {
            return fail;
        }

        public String getWaiting() {
            return waiting;
        }

        public String getMisunderstanding() {
            return misunderstandingText;
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

        public String getMeetCondition() {
            return meetCondition;
        }

        public String getBlackList() {
            return blackList;
        }
    }

    public static class Commands {
        @JsonProperty("start")
        private String start;

        @JsonProperty("misunderstanding")
        private String misunderstandingCommand;

        @JsonProperty("help")
        private String help;

        public String getStart() {
            return start;
        }

        public String getMisunderstanding() {
            return misunderstandingCommand;
        }

        public String getHelp() {
            return help;
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

        public String getMalicious() {
            return malicious;
        }

        public String getTypeUnsupported() {
            return typeUnsupported;
        }

        public String getFailure() {
            return failure;
        }

        public String getUndetected() {
            return undetected;
        }

        public String getSuspicious() {
            return suspicious;
        }

        public String getHarmless() {
            return harmless;
        }

        public String getTimeout() {
            return timeout;
        }
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