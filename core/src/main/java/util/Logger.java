package util;

import util.LogLevel;

import java.text.SimpleDateFormat;


public class Logger {
    private static final String RESET = "\u001b[0m";
    private static final String BLACK = "\u001b[30m";
    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String YELLOW = "\u001b[33m";
    private static final String BLUE = "\u001b[34m";
    private static final String MAGENTA = "\u001b[35m";
    private static final String CYAN = "\u001b[36m";
    private static final String WHITE = "\u001b[37m";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static void log(String message) {
        // [2024-01-01 12:30:45.345][threadName] Info: naš message
        log(message, LogLevel.INFO);
    }

    public static void log(String message, LogLevel level) {
        // [2024-01-01 12:30:45.345][threadName] Info: naš message
        String formatedDate = dateFormat.format(System.currentTimeMillis());
        String threadName = Thread.currentThread().getName();
        String messagePrefix = "[" + formatedDate + "][" + threadName + "] " + level + "|> ";

        switch (level) {
            case INFO -> messagePrefix  = YELLOW + messagePrefix + RESET;
            case DEBUG -> messagePrefix = MAGENTA + messagePrefix + RESET;
            case WARNING -> messagePrefix = CYAN + messagePrefix + RESET;
            case ERROR -> messagePrefix = RED + messagePrefix + RESET;
            case SUCCESS -> messagePrefix = GREEN + messagePrefix + RESET;
            case STATUS -> messagePrefix = BLUE + messagePrefix + RESET;
        }

        System.out.println(messagePrefix + message);
    }

    public static void chat(String senderName, String body) {
        System.out.println(CYAN + "[" + senderName + "] " + body + RESET);
    }
}
