package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.utils.JwtService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * Handles HTTP POST requests for user authorization.
 * It receives JSON containing user credentials, validates them, and responds with appropriate messages based on the authorization result.
 */
@WebServlet("/authorisation")
public class AuthorisationServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private PlayerService service;
    private JwtService jwtService;

    /**
     * Initializes the servlet by retrieving necessary objects from the ServletConfig.
     *
     * @param config The ServletConfig containing servlet configuration information.
     */
    @Override
    public void init(ServletConfig config) {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        service = (PlayerService) config.getServletContext().getAttribute("playerService");
        jwtService = (JwtService) config.getServletContext().getAttribute("jwtService");
    }

    /**
     * Handles HTTP POST requests. Reads user credentials from the request, validates them, and sends appropriate responses.
     *
     * @param req  The {@link HttpServletRequest} object representing the request.
     * @param resp The {@link HttpServletResponse} object representing the response.
     * @throws IOException If an I/O error occurs during request processing.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            String json = req.getReader().lines().collect(joining());
            CredentialsDto credentials = objectMapper.readValue(json, CredentialsDto.class);
            if (Utils.isValid(credentials)) {
                Optional<LoggedInPlayerDto> player = service.doAuthorisation(credentials);
                if (player.isPresent()) {
                    LoggedInPlayerDto playerDto = player.get();
                    req.getSession().setAttribute("Player", playerDto);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    Map<String, String> responseMap = new HashMap<>();
                    responseMap.put("Token", jwtService.generateToken(playerDto));
                    responseMap.put("Message", "Success authorization!");
                    resp.getWriter().write(objectMapper.writeValueAsString(responseMap));
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