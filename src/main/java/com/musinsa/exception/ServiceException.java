package com.musinsa.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ServiceException {

    private String message;
    private int errorCode;

    public ServiceException(String message, HttpStatus status){
        this.message = message;
        this.errorCode = status.value();
    }

}
