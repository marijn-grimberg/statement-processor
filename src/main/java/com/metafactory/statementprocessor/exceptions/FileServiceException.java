package com.metafactory.statementprocessor.exceptions;

public class FileServiceException extends RuntimeException {
    public FileServiceException(String message, Throwable e) {
        super(message, e);
    }
}
