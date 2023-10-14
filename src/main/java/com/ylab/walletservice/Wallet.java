package com.ylab.walletservice;

import com.ylab.walletservice.infrastructure.InMemoryPlayerRepository;
import com.ylab.walletservice.infrastructure.InMemoryTransactionRepository;
import com.ylab.walletservice.presentation.in.ConsoleProcessor;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;

import java.io.IOException;

public class Wallet {
    public static void main(String[] args) throws IOException {
        ConsoleProcessor processor = new ConsoleProcessor(new PlayerService(new InMemoryPlayerRepository()),
                new TransactionService(new InMemoryTransactionRepository()));
        processor.start();
    }
}