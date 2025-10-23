package com.otz.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	 
    // Handle custom ApiException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiExceptionResponse> handleApiException(ApiException ex, HttpServletRequest request) {
        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatusCode())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode()));
    }

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiExceptionResponse> handleValidationException(MethodArgumentNotValidException ex,
                                                                          HttpServletRequest request) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(400)
                .error(errors)
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle authentication failures
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiExceptionResponse> handleBadCredentials(BadCredentialsException ex,
                                                                     HttpServletRequest request) {
        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(401)
                .error("Invalid credentials")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle access denied
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiExceptionResponse> handleAccessDenied(AccessDeniedException ex,
                                                                   HttpServletRequest request) {
        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(403)
                .error("Access Denied")
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ApiExceptionResponse response = ApiExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(500)
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
