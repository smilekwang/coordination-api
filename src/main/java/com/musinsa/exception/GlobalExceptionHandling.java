package com.musinsa.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandling {
    @ResponseBody
    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<ErrorMessage> httpClientErrorException(HttpClientErrorException ex) {

        return new ResponseEntity<>(new ErrorMessage(ex.getStatusCode().value(), ex.getStatusText()), ex.getStatusCode());
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    protected ResponseEntity<ErrorMessage> missingServletRequestParameterException(MissingServletRequestParameterException ex) {

        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    protected ResponseEntity<ErrorMessage> httpMessageNotReadableException(HttpMessageNotReadableException ex) {

        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ApplicationException.class})
    protected ResponseEntity<ErrorMessage> applicationException(ApplicationException ex) {

        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BindException.class})
    protected ResponseEntity<ErrorMessage> bindException(BindException ex) {
        ErrorMessage errorMessage = new ErrorMessage( HttpStatus.BAD_REQUEST, buildErrorMessage(ex.getBindingResult()));

        return new ResponseEntity<>( errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(new ErrorMessage(HttpStatus.BAD_REQUEST, buildErrorMessage(ex.getBindingResult())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<?> otherException(Exception ex) {
        if (ex instanceof DataAccessException) {
            return new ResponseEntity(new ServiceException(ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity(new ServiceException(ex.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildErrorMessage(BindingResult bindingResult) {
        Map<String, Object> fieldErrorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> fieldErrorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return fieldErrorMap.keySet().stream()
                .map(key -> key + " : " + fieldErrorMap.get(key))
                .collect(Collectors.joining(" "));
    }
}
