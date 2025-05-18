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

@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleTypeMismatch(
            HttpServletRequest request
    ) {
        String message = "Parâmetro inválido. Verifique o formato e tente novamente.";
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest
            request) {

        // Collect all validation errors as a field -> message
        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing, // If duplicate keys, keep the first
                        LinkedHashMap::new // Maintain insertion order
                ));

        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, "Validation failed for one or more fields.", request);
        errorBody.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleEmailAlreadyExists(
            EmailAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleLoginAlreadyExists(
            LoginAlreadyExistsException ex,
            HttpServletRequest request
    ) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    @ExceptionHandler(DuplicatedAddressException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDuplicatedAddress(
            DuplicatedAddressException ex,
            HttpServletRequest request
    ) {
        return getObjectResponseEntity(request, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException
                                                              ex, HttpServletRequest request) {
        Map<String, Object> errorBody = buildErrorBody(
                HttpStatus.BAD_REQUEST,
                "Parâmetro obrigatório ausente: " + ex.getParameterName(),
                request
        );
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> getObjectResponseEntity(HttpServletRequest request, String message) {
        Map<String, Object> errorBody = buildErrorBody(HttpStatus.CONFLICT, message, request);
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

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

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidPasswordException(
            InvalidPasswordException ex,
            HttpServletRequest request) {

        Map<String, Object> errorBody = buildErrorBody(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }


}
