package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersRequestDTO;
import com.fiap.tech_challenge.parte1.ms_users.dtos.UsersResponseDTO;
import com.fiap.tech_challenge.parte1.ms_users.services.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersService service;

    public UsersController(UsersService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody UsersRequestDTO dto) {
        service.createUser(dto);
        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}
