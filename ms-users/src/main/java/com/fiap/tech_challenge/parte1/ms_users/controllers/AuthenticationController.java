package com.fiap.tech_challenge.parte1.ms_users.controllers;

import com.fiap.tech_challenge.parte1.ms_users.dtos.AuthenticationDataDTO;
import com.fiap.tech_challenge.parte1.ms_users.entities.User;
import com.fiap.tech_challenge.parte1.ms_users.infra.security.TokenJWTInfoDTO;
import com.fiap.tech_challenge.parte1.ms_users.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid AuthenticationDataDTO data){
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenJWTInfoDTO(tokenJWT));
    }
}
