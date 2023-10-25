package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
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
        resp.setContentType("application/json");
        try {
            String json = req.getReader().lines().collect(joining());
            CredentialsDto credentials = objectMapper.readValue(json, CredentialsDto.class);
            if (Utils.isValid(credentials)) {
                Optional<LoggedInPlayerDto> player = service.doAuthorisation(credentials);
                if (player.isPresent()) {
                    // todo add JWT
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.getOutputStream().write(objectMapper.writeValueAsBytes("Success authorization!"));
                    req.getSession().setAttribute("Player", player.get());
                } else {
                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    resp.getOutputStream().write(objectMapper.writeValueAsBytes("Authorization failed!"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Credentials validation error!"));
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().write(objectMapper.writeValueAsBytes("Internal server error!"));
            e.printStackTrace();
        }
    }
}