package com.musinsa.exception;

public class ApplicationException extends RuntimeException {

    private final ErrorMessageType errorMessageType;

    public ApplicationException(ErrorMessageType errorMessageType) {
        this.errorMessageType = errorMessageType;
    }

    @Override
    public String getMessage(){
        return errorMessageType.getErrorMessage();
    }

    public ErrorMessageType getErrorMessageType() {
        return errorMessageType;
    }
}
