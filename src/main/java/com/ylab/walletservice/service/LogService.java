package com.ylab.walletservice.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Logging service to store messages in memory.
 * It allows adding messages to a history list and retrieving the entire history.
 */
public class LogService {
    /**
     * A list to store log messages.
     */
    private static final List<String> history = new ArrayList<>();

    /**
     * Adds a new log message to the history list.
     *
     * @param message the log message to be added
     */
    static public void add(String message) {
        history.add(message);
    }

    /**
     * Retrieves the entire history of log messages.
     *
     * @return a list containing all log messages
     */
    static public List<String> getAll() {
        return history;
    }
}