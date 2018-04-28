package com.montivero.poc.resource.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message {

    private static Map<String, Message> CACHE = new HashMap<>();
    private static Function<String, Message> CALL_CONSTRUCTOR_FUNCTION = new Function<String, Message>() {
        @Override
        public Message apply(String messageValue) {
            return new Message(messageValue);
        }
    };

    private String value;

    public static Message makeMessage(String messageValue) {
        return CACHE.computeIfAbsent(messageValue, CALL_CONSTRUCTOR_FUNCTION);
    }

}
