package com.internal.ems.common.exception;

import java.time.Instant;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {

                log.warn("RESOURCE_NOT_FOUND: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse(
                                                "RESOURCE_NOT_FOUND",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                Instant.now()));

        }

        @ExceptionHandler(DuplicateResourceException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
                        HttpServletRequest request) {

                log.warn("DUPLICATE_RESOURCE: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new ErrorResponse(
                                                "DUPLICATE_RESOURCE",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                Instant.now()));

        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                String validationErrors = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));
                log.warn("Validation failed for request {}: {}", request.getRequestURI(), validationErrors);
                return ResponseEntity.badRequest()
                                .body(new ErrorResponse("VALIDATION_ERROR", validationErrors, request.getRequestURI(),
                                                Instant.now()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
                log.error("Unhandled exception at {}", request.getRequestURI(), ex);
                return ResponseEntity.internalServerError()
                                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred",
                                                request.getRequestURI(), Instant.now()));
        }

        @ExceptionHandler(ResourceConflictException.class)
        public ResponseEntity<ErrorResponse> handleResourceConflict(
                        ResourceConflictException ex,
                        HttpServletRequest request) {

                log.warn("RESOURCE_CONFLICT: {}", ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new ErrorResponse(
                                                "RESOURCE_CONFLICT",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                Instant.now()));
        }

}
