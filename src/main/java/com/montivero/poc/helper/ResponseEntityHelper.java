package com.montivero.poc.helper;

import com.montivero.poc.resource.domain.Location;
import com.montivero.poc.resource.domain.Message;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseEntityHelper {

    public static ResponseEntity prepareResponseEntityForLocation(Location location) {
        return location.getName() != null
                ? new ResponseEntity<>(location, HttpStatus.OK)
                : new ResponseEntity<>(new Message("Not found Match"), HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity prepareResponseEntityForList(List<?> list) {
        return !list.isEmpty()
                ? new ResponseEntity<>(list, HttpStatus.OK)
                : new ResponseEntity<>(new Message("Not found Match"), HttpStatus.NOT_FOUND);
    }

}
