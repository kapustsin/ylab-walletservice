package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.service.PlayerService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private PlayerService playerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        playerService = (PlayerService) config.getServletContext().getAttribute("playerService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            String json = req.getReader().lines().collect(joining());
            RegistrationDto registrationData = objectMapper.readValue(json, RegistrationDto.class);
            if (playerService.create(registrationData)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Success registration!"));
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Registration failed!"));
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().write(objectMapper.writeValueAsBytes("Internal server error!"));
            e.printStackTrace();
        }
    }
}