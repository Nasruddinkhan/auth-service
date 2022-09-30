package com.mypractice.estudy.auth.exception;

import com.mypractice.estudy.auth.model.dto.ErrorDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.Locale;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Qualifier
    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(final MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDetailsDto> notFoundException(final BadCredentialsException notFoundException, final Locale locale) {
        var error  = new ErrorDetailsDto(notFoundException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetailsDto> notFoundException(final ResourceNotFoundException notFoundException, final Locale locale) {
        var error  = new ErrorDetailsDto(notFoundException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value(), new Date());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}
