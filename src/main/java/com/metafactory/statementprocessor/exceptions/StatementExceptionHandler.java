package com.metafactory.statementprocessor.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class StatementExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(FileServiceException.class)
    public ResponseEntity<Void> handleFileServiceException(FileServiceException e) {
        log.error(e.getMessage(), e.getCause());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(StatementServiceException.class)
    public ResponseEntity<Void> handleStatementServiceException(StatementServiceException e) {
        log.error(e.getMessage(), e.getCause());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e.getCause());
        return ResponseEntity.badRequest().build();
    }
}
