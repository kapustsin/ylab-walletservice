package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.Player;
import com.ylab.walletservice.service.PlayerService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@WebServlet("/authorisation")
public class AuthorisationServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private PlayerService service;

    @Override
    public void init(ServletConfig config) {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        service = (PlayerService) config.getServletContext().getAttribute("playerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String json = req.getReader().lines().collect(joining());
        Optional<Player> player = service.doAuthorisation(req.getParameter("login"), req.getParameter("password"));
        if (player.isPresent()) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}