package com.montivero.poc.resource.validator;

import com.montivero.poc.resource.domain.exception.ValidationException;

import java.util.Objects;

public interface Validator<T> {

    String LENGTH_MAX_MESSAGE_FORMAT = "%s max length is %d characters";
    String LENGTH_RANGE_MESSAGE_FORMAT = "%s need to have length between %d and %d";
    String LENGTH_SPECIFIC_MESSAGE_FORMAT = "%s need to have the length of %d";
    String NULL_MESSAGE_FORMAT = "%s is null";

    void validate(T t);

    default <S> void validateNotNull(S t, String field) {
        if (Objects.isNull(t)) {
            throw new ValidationException(field, null, nullMessage(field));
        }
    }

    default String nullMessage(String field) {
        return String.format(NULL_MESSAGE_FORMAT, field);
    }

    default String lengthMaxMessage(String field, int max) {
        return String.format(LENGTH_MAX_MESSAGE_FORMAT, field, max);
    }

    default String lengthRangeMessage(String field, int min, int max) {
        return String.format(LENGTH_RANGE_MESSAGE_FORMAT, field, min, max);
    }

    default String lengthSpecificMessage(String field, int specificLength) {
        return String.format(LENGTH_SPECIFIC_MESSAGE_FORMAT, field, specificLength);
    }

}
