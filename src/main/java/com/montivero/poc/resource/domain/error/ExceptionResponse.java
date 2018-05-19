package com.montivero.poc.resource.domain.error;

import lombok.Getter;

@Getter
public class ExceptionResponse implements SimpleException {

    private String key;
    private String value;
    private String message;

    public ExceptionResponse(SimpleException simpleException) {
        this.key = simpleException.getKey();
        this.value = simpleException.getValue();
        this.message = simpleException.getMessage();
    }
}
