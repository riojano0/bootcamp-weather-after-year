package com.montivero.poc.resource.domain.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidationException extends RuntimeException implements SimpleException {

    private String key;
    private String value;
    private String message;

}
