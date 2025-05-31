package com.fiap.tech_challenge.parte1.ms_users.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler to centralize and customize exception handling across
 * all controllers in the application.
 * <p>
 * It intercepts specific exceptions and builds detailed error responses with HTTP status,
 * timestamps, error messages, and request details to be sent back to clients.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions when a user resource is not found.
     *
     * @param ex      the exception thrown when the user is not found
     * @param request the current HTTP request
     * @return a ResponseEntity with status 404 and error details in the body
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", HttpStatus.NOT_FOUND.value());
        errorBody.put("error", "Not Found");
        errorBody.put("message", ex.getMessage());
        errorBody.put("path", request.getRequestURI());
        errorBody.put("method", request.getMethod());

        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions caused by invalid type for method arguments,
     * for example when a path variable or request parameter cannot be converted to the expected type.
     *
     * @param request the current HTTP request
     * @return a ResponseEntity with status 400 and a generic invalid parameter message
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleTypeMismatch(HttpServletRequest request) {
        String message = "Parâmetro inválido. Verifique o formato e tente novamente.";
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions triggered when method arguments fail validation,
     * such as @Valid annotated DTOs with invalid fields.
     *
     * @param ex      the MethodArgumentNotValidException containing validation errors
     * @param request the current HTTP request
     * @return a ResponseEntity with status 400 containing field-specific validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Collect field errors with field name as key and error message as value
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields.", request);
        errorBody.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles exceptions when an email already exists in the system.
     *
     * @param ex      the EmailAlreadyExistsException
     * @param request the current HTTP request
     * @return a ResponseEntity with status 409 and the exception message
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    /**
     * Handles exceptions when a login already exists in the system.
     *
     * @param ex      the LoginAlreadyExistsException
     * @param request the current HTTP request
     * @return a ResponseEntity with status 409 and the exception message
     */
    @ExceptionHandler(LoginAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleLoginAlreadyExists(LoginAlreadyExistsException ex, HttpServletRequest request) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    /**
     * Handles exceptions when a duplicate address is detected.
     *
     * @param ex      the DuplicatedAddressException
     * @param request the current HTTP request
     * @return a ResponseEntity with status 409 and the exception message
     */
    @ExceptionHandler(DuplicatedAddressException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicatedAddress(DuplicatedAddressException ex, HttpServletRequest request) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    /**
     * Handles exceptions triggered when a required request parameter is missing.
     *
     * @param ex      the MissingServletRequestParameterException
     * @param request the current HTTP request
     * @return a ResponseEntity with status 400 and a message identifying the missing parameter
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex, HttpServletRequest request) {
        Map<String, Object> errorBody = buildErrorBody(
                HttpStatus.BAD_REQUEST,
                "Parâmetro obrigatório ausente: " + ex.getParameterName(),
                request
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to build a ResponseEntity with HTTP 409 CONFLICT status and the given message.
     *
     * @param request the current HTTP request
     * @param message the message to be included in the response body
     * @return ResponseEntity with status 409 and message body
     */
    private ResponseEntity<Object> getObjectResponseEntity(HttpServletRequest request, String message) {
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.CONFLICT, message, request);
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

    /**
     * Helper method to build a consistent error response body.
     *
     * @param status  the HTTP status to return
     * @param message the error message to include
     * @param request the current HTTP request
     * @return a map containing error details including timestamp, status, error reason, message, path, and HTTP method
     */
    private Map<String, Object> buildErrorBody(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", message);
        errorBody.put("path", request.getRequestURI());
        errorBody.put("method", request.getMethod());
        return errorBody;
    }

    /**
     * Handles exceptions when a password is invalid according to business rules.
     *
     * @param ex      the InvalidPasswordException
     * @param request the current HTTP request
     * @return a ResponseEntity with status 400 and the exception message
     */
    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidPasswordException(InvalidPasswordException ex, HttpServletRequest request) {
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }
}
