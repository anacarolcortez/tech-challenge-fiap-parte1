package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.*;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.dtos.TokenJWTInfoDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getById(@PathVariable UUID id) {
        logger.info("/findById -> {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> findAllUsers(
            @RequestParam int size,
            @RequestParam int page
    ) {
        logger.info("/findAllUsers -> size: {} ,  offset: {}", size, page);
        var allUsers = this.service.findAllUsers(size, page);
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<TokenJWTInfoDTO> create(@RequestBody @Valid UsersRequestDTO dto) {
        logger.info("/createUser -> {}", dto);
        service.createUser(dto);
        return ResponseEntity.ok(new TokenJWTInfoDTO(tokenService.generateToken(dto.login())));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJWTInfoDTO> executeLogin(@RequestBody @Valid AuthenticationDataDTO data) {
        logger.info("/login -> {}", data);
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken(((User) authentication.getPrincipal()).getLogin());
        return ResponseEntity.ok(new TokenJWTInfoDTO(tokenJWT));
    }

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

    @PatchMapping("/{id}/password")
    public ResponseEntity<String> changePassword(
            @PathVariable UUID id,
            @RequestBody @Valid ChangePasswordRequestDTO dto
    ) {
        logger.info("/changePassword -> id: {}", id);
        service.changePassword(id, dto);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }
}
