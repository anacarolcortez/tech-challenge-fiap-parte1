package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.*;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.services.TokenService;
import com.fiap.tech_challenge.parte1.ms_users.services.UsersService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing users.
 * <p>
 * Provides endpoints to create, update, retrieve, activate/deactivate users,
 * and handle authentication-related operations.
 * </p>
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService service;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public UsersController(UsersService service, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.service = service;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id UUID of the user to retrieve
     * @return ResponseEntity containing the user data if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getById(@PathVariable UUID id) {
        logger.info("/findById -> {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Retrieves a paginated list of users.
     *
     * @param size number of users per page
     * @param page page index (zero-based)
     * @return ResponseEntity with the list of users
     */
    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> findAllUsers(
            @RequestParam int size,
            @RequestParam int page
    ) {
        logger.info("/findAllUsers -> size: {} ,  offset: {}", size, page);
        var allUsers = this.service.findAllUsers(size, page);
        return ResponseEntity.ok(allUsers);
    }

    /**
     * Creates a new user.
     *
     * @param dto User creation request data validated automatically
     * @return ResponseEntity containing the created user and a generated JWT token
     */
    @PostMapping
    public ResponseEntity<CreateUserDTO> create(@RequestBody @Valid UsersRequestDTO dto) {
        logger.info("/createUser -> {}", dto);
        UsersResponseDTO user = service.createUser(dto);
        return ResponseEntity.ok(new CreateUserDTO(user, tokenService.generateToken(dto.login())));
    }

    /**
     * Authenticates a user and returns a JWT token if successful.
     *
     * @param data User login credentials validated automatically
     * @return ResponseEntity containing the JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<TokenJWTInfoDTO> executeLogin(@RequestBody @Valid AuthenticationDataDTO data) {
        logger.info("/login -> {}", data);
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken(((User) authentication.getPrincipal()).getLogin());
        return ResponseEntity.ok(new TokenJWTInfoDTO(tokenJWT));
    }

    /**
     * Toggles user activation status.
     *
     * @param id       UUID of the user to activate/deactivate
     * @param activate true to activate, false to deactivate
     * @return ResponseEntity with confirmation message
     */
    @PatchMapping("/{id}")
    public ResponseEntity<String> toggleActivation(
            @PathVariable UUID id,
            @RequestParam boolean activate
    ) {
        logger.info("/toggleActivation -> id: {}, activate: {}", id, activate);

        if (activate) {
            service.reactivateUser(id);
            return ResponseEntity.ok("Usuário ativado com sucesso!");
        } else {
            service.deactivateUser(id);
            return ResponseEntity.ok("Usuário desativado com sucesso!");
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param id  UUID of the user whose password is to be changed
     * @param dto Change password request containing old and new passwords
     * @return ResponseEntity with confirmation message
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable UUID id,
            @RequestBody @Valid ChangePasswordRequestDTO dto
    ) {
        logger.info("/changePassword -> id: {}", id);
        service.changePassword(id, dto);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    /**
     * Updates user details.
     *
     * @param id  UUID of the user to update
     * @param dto Update user request data validated automatically
     * @return ResponseEntity containing the updated user data
     */
    @PutMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto) {
        logger.info("/updateUser -> id: {}, body: {}", id, dto);
        var updatedUser = service.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

}
