package com.fiap.tech_challenge.parte1.ms_users.exceptions;

/**
 * Exception thrown when JWT token generation fails.
 * This exception wraps the underlying cause of the failure.
 */
public class TokenGenerationException extends RuntimeException {

  /**
   * Constructs a new TokenGenerationException with the specified detail message and cause.
   *
   * @param message the detail message explaining the token generation failure
   * @param cause   the underlying cause of the exception
   */
  public TokenGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
