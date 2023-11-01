package com.ylab.walletservice.presentation.in.controller;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.TransactionService;
import com.ylab.walletservice.service.utils.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class handling transactions-related HTTP requests.
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final PlayerService playerService;
    private final TransactionService transactionService;
    private final JwtService jwtService;

    /**
     * Constructs a new TransactionController with the specified PlayerService, TransactionService,
     * and JwtService instances.
     *
     * @param playerService     The PlayerService instance responsible for player-related operations.
     * @param transactionService The TransactionService instance responsible for transaction-related operations.
     * @param jwtService        The JwtService instance responsible for generating JWT tokens.
     */
    public TransactionController(PlayerService playerService, TransactionService transactionService,
            JwtService jwtService) {
        this.playerService = playerService;
        this.transactionService = transactionService;
        this.jwtService = jwtService;
    }

    /**
     * Handles HTTP GET requests for retrieving transaction history of the logged-in player.
     *
     * @param player The LoggedInPlayerDto object representing the logged-in player.
     * @return ResponseEntity containing a list of Transaction objects representing the player's transaction history.
     */
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory(@RequestAttribute LoggedInPlayerDto player) {
        try {
            return new ResponseEntity<>(transactionService.getHistory(player.id()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles HTTP POST requests for creating a new transaction for the logged-in player.
     *
     * @param player             The LoggedInPlayerDto object representing the logged-in player.
     * @param transactionRequest The TransactionRequestDto object containing transaction request data from the request body.
     * @return ResponseEntity with a JSON message indicating the transaction creation status.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestAttribute LoggedInPlayerDto player, @RequestBody
    TransactionRequestDto transactionRequest) {
        try {
            if (Utils.isValid(transactionRequest, player.id())) {
                if (transactionService.create(transactionRequest)) {
                    return new ResponseEntity<>("Transaction created successfully!", HttpStatus.CREATED);
                } else {
                    return new ResponseEntity<>("Transaction creation failed!", HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>("Transaction validation error!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}