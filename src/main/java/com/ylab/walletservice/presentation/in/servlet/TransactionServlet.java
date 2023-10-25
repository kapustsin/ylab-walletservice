package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.TransactionRequestDto;
import com.ylab.walletservice.service.TransactionService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

@WebServlet("/transactions")
public class TransactionServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private TransactionService transactionService;

    @Override
    public void init(ServletConfig config) {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        transactionService = (TransactionService) config.getServletContext().getAttribute("transactionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        LoggedInPlayerDto playerDto = (LoggedInPlayerDto) req.getSession().getAttribute("Player");
        try {
            if (playerDto != null) {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        LoggedInPlayerDto playerDto = (LoggedInPlayerDto) req.getSession().getAttribute("Player");
        try {
            if (playerDto != null) {
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