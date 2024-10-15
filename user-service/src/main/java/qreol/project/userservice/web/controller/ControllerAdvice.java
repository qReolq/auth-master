package qreol.project.userservice.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import qreol.project.userservice.model.exception.ResourceNotFoundException;
import qreol.project.userservice.model.exception.ResourceNotValidException;
import qreol.project.userservice.model.exception.RoleException;
import qreol.project.userservice.web.dto.exception.ExceptionBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handleResourceNotFound(ResourceNotFoundException e) {
        log.error("Resource not found: {}", e.getMessage());
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        log.error("Validation failed: {}", e.getMessage());
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed", LocalDateTime.now());
        exceptionBody.setErrors(mapListToMap(e.getBindingResult().getFieldErrors()));

        return exceptionBody;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handleException(Exception e) {
        log.error("Role exception: {}", e.getMessage());
        return new ExceptionBody("Internal error", LocalDateTime.now());
    }


    @ExceptionHandler(RoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleRoleAlreadyAssigned(RoleException e) {
        log.error("Resource validation failed: {}", e.getMessage());
        return new ExceptionBody(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(ResourceNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handleConstraintViolation(ResourceNotValidException e) {
        ExceptionBody exceptionBody = new ExceptionBody("Validation failed", LocalDateTime.now());
        exceptionBody.setErrors(mapListToMap(e.getErrors()));

        return exceptionBody;
    }

    private Map<String, String> mapListToMap(List<FieldError> errors) {
        return errors
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

}
