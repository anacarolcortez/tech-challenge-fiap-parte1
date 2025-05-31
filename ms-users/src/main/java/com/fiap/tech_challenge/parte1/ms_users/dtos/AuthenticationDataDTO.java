package com.fiap.tech_challenge.parte1.ms_users.dtos;

/**
 * Data Transfer Object for authentication requests.
 * Holds the login and password credentials submitted by a user
 * during authentication.
 *
 * @param login    the user's login identifier
 * @param password the user's password
 */
public record AuthenticationDataDTO(String login, String password) {
}
