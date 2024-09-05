package com.musinsa.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
class ErrorMessage {

    private int errorCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Object> errorObjectList;

    ErrorMessage(HttpStatus errorCode, String message){

        this.errorCode = errorCode.value();
        this.message = message;
    }

    ErrorMessage(Integer errorCode, String message){

        this.errorCode = errorCode;
        this.message = message;
    }
}
