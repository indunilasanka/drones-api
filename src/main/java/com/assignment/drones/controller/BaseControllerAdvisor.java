package com.assignment.drones.controller;

import com.assignment.drones.exception.RuntimeValidationException;
import com.assignment.drones.model.dto.BaseResponseDTO;
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

import static com.assignment.drones.util.Constants.RequestStatus.ERROR;
import static com.assignment.drones.util.Constants.RequestStatus.FAILURE;
import static org.springframework.http.HttpStatus.*;

/**
 * Controller advice which capture exceptions/errors and return custom error messages
 */
@ControllerAdvice
public abstract class BaseControllerAdvisor {
    private final Logger LOGGER = LoggerFactory.getLogger(BaseControllerAdvisor.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.keySet().stream().map(key -> key + "=" + errors.get(key))
                .collect(Collectors.joining("; ", "", ""));
        LOGGER.error("validation errors: {}", errorMessage);

        BaseResponseDTO response = new BaseResponseDTO();
        response.setStatus(ERROR);
        response.setMessage(errorMessage);
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeValidationException.class)
    public ResponseEntity<BaseResponseDTO> handleRuntimeValidationExceptions(RuntimeValidationException ex) {
        LOGGER.error("Run time validation failure: {}", ex.getMessage());

        BaseResponseDTO response = new BaseResponseDTO();
        response.setStatus(FAILURE);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponseDTO> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        LOGGER.error("Resource not found error: {}", ex.getMessage());

        BaseResponseDTO response = new BaseResponseDTO();
        response.setStatus(ERROR);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponseDTO> handleMethodNotSupportedExceptios(HttpRequestMethodNotSupportedException ex) {
        LOGGER.error("Method not found error: {}", ex.getMessage());

        BaseResponseDTO response = new BaseResponseDTO();
        response.setStatus(ERROR);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDTO> handleServerErrors(Exception ex) {
        LOGGER.error("Server error : {}", ExceptionUtils.getStackTrace(ex));

        BaseResponseDTO response = new BaseResponseDTO();
        response.setStatus(ERROR);
        response.setMessage("Server error: contact server admin");
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }
}
