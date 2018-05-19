package com.montivero.poc.configuration;

import com.montivero.poc.resource.domain.error.ExceptionResponse;
import com.montivero.poc.resource.domain.error.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerControllerConfiguration {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandlerControllerConfiguration.class);

    @ResponseBody
    @ExceptionHandler({ValidationException.class})
    public ResponseEntity validationExceptionHandler(ValidationException validationException) {
        logger.error("Validation Exception", validationException.getLocalizedMessage());
        ExceptionResponse exceptionResponse = new ExceptionResponse(validationException);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

}
