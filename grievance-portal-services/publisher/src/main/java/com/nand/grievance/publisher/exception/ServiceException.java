package com.nand.grievance.publisher.exception;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private String errorMessage;

    public ServiceException() {
    }

    public ServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
