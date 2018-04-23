package com.montivero.poc.resource;

import com.montivero.poc.delegate.StateDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("state")
public class StateResource {

    private final StateDelegate stateDelegate;

    @Autowired
    public StateResource(StateDelegate stateDelegate) {
        this.stateDelegate = stateDelegate;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{country}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllStatesByCountry(@PathVariable(value = "country") String country) {
        return stateDelegate.getAllStatesByCountry(country);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{country}/{shortStateName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getStateByCountryAndShortName(@PathVariable(value = "country") String country, @PathVariable(value = "shortStateName") String shortStateName) {
        return stateDelegate.getStateByCountryAndShortName(country, shortStateName);
    }

}
