package com.montivero.poc.resource.validator;

import com.montivero.poc.resource.domain.error.ValidationException;

import java.util.Objects;

public interface Validator<T>  {

    void validate(T t);

    default <S> void validateNotNull(S t, String field) {
        if (Objects.isNull(t)) {
            throw new ValidationException(field, null, "Can't be null ");
        }
    }

}
