package com.assignment.drones.controller;

import com.assignment.drones.dto.ErrorResponseDTO;
import com.assignment.drones.exception.RuntimeValidationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public abstract class BaseController {
    private final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(BAD_REQUEST.value());
        response.setMessage(errors.keySet().stream()
                .map(key -> key + "=" + errors.get(key))
                .collect(Collectors.joining("; ", "", "")));
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeValidationExceptions(RuntimeValidationException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(BAD_REQUEST.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotSupportedExceptios(HttpRequestMethodNotSupportedException ex) {
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(METHOD_NOT_ALLOWED.value());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleServerErrors(Exception ex) {
        logger.error("Server error : {}", ExceptionUtils.getStackTrace(ex));
        ErrorResponseDTO response = new ErrorResponseDTO();
        response.setCode(INTERNAL_SERVER_ERROR.value());
        response.setMessage("Server error: contact server admin");
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
