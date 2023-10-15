package com.ylab.walletservice.presentation.in;

import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Handles user interactions through the console interface for player registration,
 * authorization, and transaction processing.
 */
public class ConsoleProcessor {
    final private Scanner scanner;
    final private PlayerService playerService;
    final private TransactionService transactionService;

    /**
     * Constructs a ConsoleProcessor object with the specified PlayerService and TransactionService instances.
     *
     * @param playerService      The PlayerService used for player-related operations.
     * @param transactionService The TransactionService used for transaction-related operations.
     */
    public ConsoleProcessor(PlayerService playerService, TransactionService transactionService) {
        this.scanner = new Scanner(System.in);
        this.playerService = playerService;
        this.transactionService = transactionService;
    }

    /**
     * Starts the console application, allowing users to register, log in, and perform transactions.
     */
    public void start() {
        System.out.println("Welcome to Wallet service!");
        System.out.println("Press:");
        System.out.println("1-Registration.");
        System.out.println("2-Authorization.");
        int i = scanner.nextInt();
        scanner.nextLine();

        if (i == 1) {
            if (doRegistration()) {
                System.out.println("Successful registration! Please authorize.");
                Optional<Player> player = doAuthorisation();
                if (player.isPresent()) {
                    doWork(player.get());
                } else {
                    System.out.println("Authorization error. Exit.");
                }
            } else {
                System.out.println("Registration failed : login exists. Exit.");
            }
        } else if (i == 2) {
            Optional<Player> player = doAuthorisation();
            if (player.isPresent()) {
                doWork(player.get());
            } else {
                System.out.println("Authorization error. Exit.");
            }
        }
    }

    /**
     * Handles the registration process by taking user input for login and password.
     *
     * @return true if registration is successful, false otherwise.
     */
    public boolean doRegistration() {
        System.out.println("Registration:");
        System.out.print("Input login:");
        String login = scanner.nextLine();
        System.out.print("Input password:");
        String password = scanner.nextLine();
        return playerService.create(login, password);
    }

    /**
     * Handles the authorization process by taking user input for login and password.
     *
     * @return An Optional containing the Player object if authorization is successful, or empty otherwise.
     */
    public Optional<Player> doAuthorisation() {
        System.out.println("Authorization:");
        System.out.print("Input login:");
        String login = scanner.nextLine();
        System.out.print("Input password:");
        String password = scanner.nextLine();
        return playerService.doAuthorisation(login, password);
    }

    /**
     * Handles user interaction after successful login, allowing players to perform debit, credit,
     * check balance, view transaction history, and exit.
     *
     * @param player The logged-in player.
     */
    public void doWork(Player player) {
        System.out.println("Hello " + player.getLogin() + "!");
        System.out.println("Press:");
        System.out.println("1-Debit");
        System.out.println("2-Credit");
        System.out.println("3-Balance");
        System.out.println("4-History");
        System.out.println("0-Exit");
        int input = scanner.nextInt();
        while (input != 0) {
            switch (input) {
                case 1:
                    System.out.println("Debit:");
                    System.out.print("Input amount:");
                    scanner.nextLine();
                    if (transactionService.create(player, new BigDecimal(scanner.nextLine()), "debit")) {
                        System.out.print("Debit complete! Balance = ");
                    } else {
                        System.out.print("Debit error! Balance = ");
                    }
                    System.out.println(playerService.getBalance(player.getLogin()));
                    break;
                case 2:
                    System.out.println("Credit:");
                    System.out.print("Input amount:");
                    scanner.nextLine();
                    if (transactionService.create(player, new BigDecimal(scanner.nextLine()), "credit")) {
                        System.out.print("Credit complete! Balance = ");
                    } else {
                        System.out.print("Credit error! Balance = ");
                    }
                    playerService.getBalance(player.getLogin());
                    System.out.println(playerService.getBalance(player.getLogin()));
                    break;
                case 3:
                    System.out.println("Balance = " + playerService.getBalance(player.getLogin()));
                    break;
                case 4:
                    System.out.println("History:");
                    List<Transaction> history = transactionService.getHistory(player.getId());
                    if (!history.isEmpty()) {
                        for (Transaction transaction : history) {
                            System.out.println(
                                    "id=" + transaction.getId() + " ,amount = " + transaction.getAmount() +
                                            " ,type = " +
                                            transaction.getType());
                        }
                    } else {
                        System.out.println("No records.");
                    }
                    break;
            }
            System.out.println("Press:");
            System.out.println("1-Debit");
            System.out.println("2-Credit");
            System.out.println("3-Balance");
            System.out.println("4-History");
            System.out.println("0-Exit");
            input = scanner.nextInt();
        }
        System.out.println("Exit.");
    }
}