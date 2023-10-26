package com.ylab.walletservice.presentation.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.walletservice.aop.annotations.Loggable;
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

@Loggable
@WebServlet("/registration")
/*
 * Handles HTTP POST requests for user registration.
 * It receives registration data in JSON format, validates the data, and attempts to create a new player using {@link PlayerService}.
 * The response contains a success message if the registration is successful, or an error message if registration fails.
 */
public class RegistrationServlet extends HttpServlet {
    private ObjectMapper objectMapper;
    private PlayerService playerService;

    @Override
    /*
     * Initializes the servlet by retrieving necessary objects from the ServletConfig.
     *
     * @param config The ServletConfig containing servlet configuration information.
     */
    public void init(ServletConfig config) throws ServletException {
        objectMapper = (ObjectMapper) config.getServletContext().getAttribute("objectMapper");
        playerService = (PlayerService) config.getServletContext().getAttribute("playerService");
    }

    @Override
    /*
     * Handles HTTP POST requests for user registration. Validates the registration data,
     * creates a new player using {@link PlayerService}, and sends a response with the result.
     *
     * @param req  The {@link HttpServletRequest} object representing the request.
     * @param resp The {@link HttpServletResponse} object representing the response.
     * @throws IOException If an I/O error occurs during request processing.
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        try {
            String json = req.getReader().lines().collect(joining());
            RegistrationDto registrationData = objectMapper.readValue(json, RegistrationDto.class);
            if (Utils.isValid(registrationData)) {
                if (playerService.create(registrationData)) {
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    resp.getOutputStream().write(objectMapper.writeValueAsBytes("Success registration!"));
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getOutputStream().write(objectMapper.writeValueAsBytes("Registration failed!"));
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getOutputStream().write(objectMapper.writeValueAsBytes("Registration data validation error!"));
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().write(objectMapper.writeValueAsBytes("Internal server error!"));
        }
    }
}