package com.ylab.walletservice.service;

import java.util.ArrayList;
import java.util.List;

public class LogService {
    private static final List<String> history = new ArrayList<>();

    static public void add(String message) {
        history.add(message);
    }

    static public List<String> getAll() {
        return history;
    }
}