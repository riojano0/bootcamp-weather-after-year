package com.montivero.poc.resource;

import com.montivero.poc.delegate.StateDelegate;
import com.montivero.poc.resource.domain.Country;
import com.montivero.poc.resource.domain.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("state")
public class StateResource {

    private final StateDelegate stateDelegate;

    @Autowired
    public StateResource(StateDelegate stateDelegate) {
        this.stateDelegate = stateDelegate;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{countryCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllStatesByCountry(@PathVariable(value = "countryCode") String country) {
        return stateDelegate.getAllStatesByCountry(country);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity saveState(@RequestBody State state) {
        return stateDelegate.saveState(state);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{countryCode}/{shortStateName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getStateByCountryAndShortName(@PathVariable(value = "countryCode") String countryCode, @PathVariable(value = "shortStateName") String shortStateName) {
        return stateDelegate.getStateByCountryAndShortName(countryCode, shortStateName);
    }

}
