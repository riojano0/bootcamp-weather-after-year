package com.montivero.poc.delegate;

import com.montivero.poc.adapter.CountryAdapter;
import com.montivero.poc.client.CountryNameClient;
import com.montivero.poc.client.domain.GroupKTCountry;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.resource.domain.Country;
import com.montivero.poc.resource.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryDelegate {

    private final CountryNameClient countryNameClient;
    private final CountryAdapter countryAdapter;

    @Autowired
    public CountryDelegate(CountryNameClient countryNameClient, CountryAdapter countryAdapter) {
        this.countryNameClient = countryNameClient;
        this.countryAdapter = countryAdapter;
    }

    public ResponseEntity getCountry(String code) {
        GroupKTResponse<GroupKTCountry> groupKTResponse;
        switch (code.length()) {
            case 2:
                groupKTResponse = countryNameClient.getCountryByIso2Code(code);
                break;
            case 3:
                groupKTResponse = countryNameClient.getCountryByIso3Code(code);
                break;
            default:
                return new ResponseEntity<>(new Message("Try with Iso2Code or Iso3Code"), HttpStatus.BAD_REQUEST);
        }
        GroupKTRestResponse<GroupKTCountry> restResponse = getRestResponse(groupKTResponse);
        GroupKTCountry groupKTCountry = getResult(restResponse);
        Country country = countryAdapter.adaptGroupKTCountryToCountry(groupKTCountry);
        return prepareResponseEntityForCountry(country);
    }

    public ResponseEntity getAllCountries() {
        GroupKTResponse<List<GroupKTCountry>> groupKTResponse = countryNameClient.getAllCountries();
        GroupKTRestResponse<List<GroupKTCountry>> restResponse = getRestResponse(groupKTResponse);
        List<GroupKTCountry> groupKTCountries = getResult(restResponse);
        List<Country> countries = countryAdapter.adaptGroupKTCountriesToCountries(groupKTCountries);
        return prepareResponseEntityForCountries(countries);
    }

    private ResponseEntity prepareResponseEntityForCountry(Country country) {
        return country.getName() != null
                ? new ResponseEntity<>(country, HttpStatus.OK)
                : new ResponseEntity<>(new Message("Not found Match"), HttpStatus.NOT_FOUND);
    }

    private ResponseEntity prepareResponseEntityForCountries(List<Country> countries) {
        return !countries.isEmpty()
                ? new ResponseEntity<>(countries, HttpStatus.OK)
                : new ResponseEntity<>(new Message("Not found Match"), HttpStatus.NOT_FOUND);
    }

    private <T> GroupKTRestResponse<T> getRestResponse(GroupKTResponse<T> groupKTResponse) {
        return groupKTResponse != null
                ? groupKTResponse.getRestResponse()
                : null;
    }

    private <T> T getResult(GroupKTRestResponse<T> groupKTRestResponse) {
        return (groupKTRestResponse != null)
                ? groupKTRestResponse.getResult()
                : null;
    }


}
