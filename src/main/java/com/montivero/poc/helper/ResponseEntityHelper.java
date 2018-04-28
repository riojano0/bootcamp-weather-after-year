package com.montivero.poc.helper;

import com.montivero.poc.resource.domain.Location;
import com.montivero.poc.resource.domain.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityHelper {

    private static final String NOT_FOUND_MATCH_MESSAGE = "Not found Match";

    public static ResponseEntity prepareResponseEntityForLocation(Location location) {
        return location.getName() != null
                ? new ResponseEntity<>(location, HttpStatus.OK)
                : new ResponseEntity<>(Message.makeMessage(NOT_FOUND_MATCH_MESSAGE), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity prepareResponseEntityForList(List<?> list) {
        return list != null && !list.isEmpty()
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(Message.makeMessage(NOT_FOUND_MATCH_MESSAGE), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity prepareResponseEntityMessage(Message message, HttpStatus httpStatus){
        return new ResponseEntity<>(message, httpStatus);
    }

}
