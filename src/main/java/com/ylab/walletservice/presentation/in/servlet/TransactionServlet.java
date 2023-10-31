package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.service.TransactionService;
import com.ylab.walletservice.service.utils.JwtService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

@WebServlet("/transactions")
/*
 * Handles HTTP GET and POST requests for player transactions.
 * It receives transaction data in JSON format, validates the data, and performs corresponding actions using {@link TransactionService}.
 * The servlet allows retrieving transaction history using HTTP GET requests and creating new transactions using HTTP POST requests.
 * The responses contain transaction information if successful, or an error message if the transaction fails.
 */
public class TransactionServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private TransactionService transactionService;
    private JwtService jwtService;

    @Override
    /*
     * Initializes the servlet by retrieving necessary objects from the ServletConfig.
     *
     * @param config The ServletConfig containing servlet configuration information.
     */
    public void init(ServletConfig config) {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        transactionService = (TransactionService) config.getServletContext().getAttribute("transactionService");
        jwtService = (JwtService) config.getServletContext().getAttribute("jwtService");
    }

    @Override
    /*
     * Handles HTTP GET requests for retrieving player transaction history.
     * Validates the JWT token and player session, then sends a response with the transaction history.
     *
     * @param req  The {@link HttpServletRequest} object representing the request.
     * @param resp The {@link HttpServletResponse} object representing the response.
     * @throws IOException If an I/O error occurs during request processing.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        LoggedInPlayerDto playerDto = (LoggedInPlayerDto) req.getSession().getAttribute("Player");
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if (playerDto != null && jwtService.isValid(token, playerDto)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getOutputStream()
                        .write(objectMapper.writeValueAsBytes(
                                "History: " + transactionService.getHistory(playerDto.id())));
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Unauthorized access!"));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().write(objectMapper.writeValueAsBytes("Internal server error!"));
        }
    }

    @Override
    /*
     * Handles HTTP POST requests for creating new player transactions.
     * Validates the JWT token, player session, and transaction data, then sends a response with the transaction result.
     *
     * @param req  The {@link HttpServletRequest} object representing the request.
     * @param resp The {@link HttpServletResponse} object representing the response.
     * @throws IOException If an I/O error occurs during request processing.
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        LoggedInPlayerDto playerDto = (LoggedInPlayerDto) req.getSession().getAttribute("Player");
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if (playerDto != null && jwtService.isValid(token, playerDto)) {
                String json = req.getReader().lines().collect(joining());
                TransactionRequestDto transactionRequest = objectMapper.readValue(json, TransactionRequestDto.class);
                if (Utils.isValid(transactionRequest, playerDto.id())) {
                    if (transactionService.create(transactionRequest)) {
                        resp.setStatus(HttpServletResponse.SC_CREATED);
                        resp.getOutputStream()
                                .write(objectMapper.writeValueAsBytes("Transaction created successfully!"));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        resp.getOutputStream().write(objectMapper.writeValueAsBytes("Transaction creation failed!"));
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getOutputStream().write(objectMapper.writeValueAsBytes("Transaction validation error!"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Unauthorized access!"));
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().write(objectMapper.writeValueAsBytes("Internal server error!"));
        }
    }
}