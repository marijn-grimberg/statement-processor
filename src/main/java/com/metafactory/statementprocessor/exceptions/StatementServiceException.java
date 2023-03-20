package com.metafactory.statementprocessor.exceptions;

public class StatementServiceException extends RuntimeException {
    public StatementServiceException(String message, Throwable e) {
        super(message, e);
    }
}
