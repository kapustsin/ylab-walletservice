package com.ylab.walletservice.presentation.in.controller;

import com.ylab.walletservice.domain.dto.CredentialsDto;
import com.ylab.walletservice.domain.dto.LoggedInPlayerDto;
import com.ylab.walletservice.domain.dto.RegistrationDto;
import com.ylab.walletservice.service.PlayerService;
import com.ylab.walletservice.service.utils.JwtService;
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

import java.util.Optional;

/**
 * Controller class handling player-related HTTP requests.
 */
@Tag(name = "player-controller",
        description = "Endpoints for player authentication, authorization, and balance management.")
@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private final PlayerService playerService;
    private final JwtService jwtService;

    /**
     * Constructs a new PlayerController with the specified PlayerService and JwtService instances.
     *
     * @param playerService The PlayerService instance responsible for player-related operations.
     * @param jwtService    The JwtService instance responsible for generating JWT tokens.
     */
    public PlayerController(PlayerService playerService, JwtService jwtService) {
        this.playerService = playerService;
        this.jwtService = jwtService;
    }

    /**
     * Handles HTTP POST requests for user authorization.
     * It receives JSON containing user credentials, validates them, and responds with appropriate messages
     * based on the authorization result.
     *
     * @param credentials The CredentialsDto object containing user credentials from the request body.
     * @return ResponseEntity with a JSON message indicating the authorization status and, if successful, a JWT token.
     */
    @Operation(summary = "Authenticate player and generate JWT token.")
    @PostMapping(value = "/authorisation")
    public ResponseEntity<String> doAuthorisation(@RequestBody CredentialsDto credentials) {
        if (Utils.isValid(credentials)) {
            Optional<LoggedInPlayerDto> player = playerService.doAuthorisation(credentials);
            if (player.isPresent()) {
                LoggedInPlayerDto playerDto = player.get();
                return new ResponseEntity<>(jwtService.generateToken(playerDto), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Authorization failed!", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Credentials validation error!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles HTTP POST requests for user registration.
     * It receives JSON containing user registration data, validates them, and responds with appropriate messages
     * based on the registration result.
     *
     * @param registrationData The RegistrationDto object containing user registration data from the request body.
     * @return ResponseEntity with a JSON message indicating the registration status.
     */
    @Operation(summary = "Register a new player.")
    @PostMapping(value = "/registration")
    public ResponseEntity<String> doRegistration(@RequestBody RegistrationDto registrationData) {
        if (Utils.isValid(registrationData)) {
            if (playerService.create(registrationData)) {
                return new ResponseEntity<>("Success registration!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Registration failed!", HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>("Registration data validation error!", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handles HTTP GET requests for retrieving player balance.
     * It requires a valid logged-in player obtained from the request attribute.
     *
     * @param player The LoggedInPlayerDto object representing the logged-in player.
     * @return ResponseEntity with a JSON message containing the player's balance.
     */
    @Operation(summary = "Get player's balance.")
    @GetMapping(value = "/balance")
    public ResponseEntity<String> getBalance(@RequestAttribute LoggedInPlayerDto player) {
        return new ResponseEntity<>("Balance = " + playerService.getBalance(player.id()), HttpStatus.OK);
    }
}