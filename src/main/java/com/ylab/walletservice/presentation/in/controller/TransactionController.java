package com.ylab.walletservice.presentation.in.controller;

import com.ylab.walletservice.domain.Transaction;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "transaction-controller", description = "Endpoints for managing player transactions.")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    /**
     * Constructs a new TransactionController with the specified TransactionService instance.
     *
     * @param transactionService The TransactionService instance responsible for transaction-related operations.
     */
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handles HTTP GET requests for retrieving transaction history of the logged-in player.
     *
     * @param player The LoggedInPlayerDto object representing the logged-in player.
     * @return ResponseEntity containing a list of Transaction objects representing the player's transaction history.
     */
    @Operation(summary = "Get transaction history of the logged-in player.")
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
    @Operation(summary = "Create a new transaction for the logged-in player.")
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