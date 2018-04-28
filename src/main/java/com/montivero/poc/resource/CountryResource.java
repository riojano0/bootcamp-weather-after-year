package com.montivero.poc.resource;

import com.montivero.poc.delegate.CountryDelegate;
import com.montivero.poc.resource.domain.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("country")
public class CountryResource {

    private final CountryDelegate countryDelegate;

    @Autowired
    public CountryResource(CountryDelegate countryDelegate) {
        this.countryDelegate = countryDelegate;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{code}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getCountryByCode(@PathVariable(value = "code") String code) {
        return countryDelegate.getCountryByCode(code);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getAllCountries() {
        return countryDelegate.getAllCountries();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity saveCountry(@RequestBody Country country) {
        return countryDelegate.saveCountry(country);
    }

}
