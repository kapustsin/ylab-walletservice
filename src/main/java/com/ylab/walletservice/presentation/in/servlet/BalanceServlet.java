package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.utils.JwtService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/balance")
public class BalanceServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private PlayerService playerService;
    private JwtService jwtService;

    @Override
    public void init(ServletConfig config) {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        playerService = (PlayerService) config.getServletContext().getAttribute("playerService");
        jwtService = (JwtService) config.getServletContext().getAttribute("jwtService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        LoggedInPlayerDto playerDto = (LoggedInPlayerDto) req.getSession().getAttribute("Player");
        String token = req.getHeader("Authorization").replace("Bearer ", "");
        try {
            if (playerDto != null && jwtService.isValid(token, playerDto)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getOutputStream()
                        .write(objectMapper.writeValueAsBytes("Balance = " + playerService.getBalance(playerDto.id())));
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