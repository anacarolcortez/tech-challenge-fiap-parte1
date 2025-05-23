package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.ChangePasswordRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UpdateUserDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.services.UsersService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getById(@PathVariable UUID id) {
        logger.info("/findById -> {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> findAllUsers(
            @RequestParam int size,
            @RequestParam int page) {
        logger.info("/findAllUsers -> size: {} ,  offset: {}", size, page);
        var allUsers = this.service.findAllUsers(size, page);
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid UsersRequestDTO dto) {
        logger.info("/createUser -> {}", dto);
        service.createUser(dto);
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> toggleActivation(
            @PathVariable UUID id,
            @RequestParam boolean activate) {
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
            @RequestBody @Valid ChangePasswordRequestDTO dto) {
        logger.info("/changePassword -> id: {}", id);
        service.changePassword(id, dto);
        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> updateUser(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateUserDTO dto) {
        logger.info("/updateUser -> id: {}, body: {}", id, dto);
        var updatedUser = service.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

}
