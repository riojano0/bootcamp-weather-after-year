package com.montivero.poc.delegate;

import com.montivero.poc.transformer.CountryTransformer;
import com.montivero.poc.client.CountryNameClient;
import com.montivero.poc.client.domain.GroupKTCountry;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
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
    private final CountryTransformer countryTransformer;

    @Autowired
    public CountryDelegate(CountryNameClient countryNameClient, CountryTransformer countryTransformer) {
        this.countryNameClient = countryNameClient;
        this.countryTransformer = countryTransformer;
    }

    public ResponseEntity getAllCountries() {
        GroupKTResponse<List<GroupKTCountry>> groupKTResponse = countryNameClient.getAllCountries();
        GroupKTRestResponse<List<GroupKTCountry>> restResponse = GroupKTHelper.getRestResponse(groupKTResponse);
        List<GroupKTCountry> groupKTCountries = GroupKTHelper.getResult(restResponse);
        List<Country> countries = countryTransformer.transformList(groupKTCountries);
        return ResponseEntityHelper.prepareResponseEntityForList(countries);
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
        GroupKTRestResponse<GroupKTCountry> restResponse = GroupKTHelper.getRestResponse(groupKTResponse);
        GroupKTCountry groupKTCountry = GroupKTHelper.getResult(restResponse);
        Country country = countryTransformer.transform(groupKTCountry);
        return ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

}
