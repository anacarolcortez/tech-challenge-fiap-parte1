package com.fiap.tech_challenge.parte1.ms_users.dtos;

import java.util.UUID;

public record UsersResponseDTO(
        UUID id,
        String name,
        String email,
        String login,
        String role
) {
}
