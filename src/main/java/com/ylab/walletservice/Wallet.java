package com.ylab.walletservice;

import com.ylab.walletservice.infrastructure.InMemoryPlayerRepository;
import com.ylab.walletservice.infrastructure.InMemoryTransactionRepository;
import com.ylab.walletservice.presentation.in.ConsoleProcessor;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;

/**
 * Main class representing the entry point of the Wallet application.
 */
public class Wallet {
    /**
     * Main method to start the Wallet application and initiate the console interface for user interaction.
     *
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        ConsoleProcessor processor = new ConsoleProcessor(new PlayerService(new InMemoryPlayerRepository()),
                new TransactionService(new InMemoryTransactionRepository()));
        processor.start();
    }
}