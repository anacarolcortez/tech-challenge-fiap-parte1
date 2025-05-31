package com.fiap.tech_challenge.parte1.ms_users.dtos;

/**
 * Data Transfer Object for carrying JWT token information.
 *
 * @param tokenJWT the JWT token string
 */
public record TokenJWTInfoDTO(String tokenJWT) {
}
