package com.montivero.poc.delegate;

import com.montivero.poc.client.CountryNameClient;
import com.montivero.poc.client.domain.GroupKTCountry;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.repository.CountryRepository;
import com.montivero.poc.resource.domain.Country;
import com.montivero.poc.resource.domain.Message;
import com.montivero.poc.transformer.CountryTransformer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CountryDelegate {

    private static final String MESSAGE_VALUE_COUNTRY_SAVED = "Country Saved";
    private static final String MESSAGE_VALUE_COUNTRY_ALREADY_SAVE = "Country Already Save";
    private static final String MESSAGE_VALUE_TRY_WITH_ISO_2_CODE_OR_ISO_3_CODE = "Try with Iso2Code or Iso3Code";

    private final CountryNameClient countryNameClient;
    private final CountryTransformer countryTransformer;
    private final CountryRepository countryRepository;

    @Autowired
    public CountryDelegate(CountryNameClient countryNameClient, CountryTransformer countryTransformer, CountryRepository countryRepository) {
        this.countryNameClient = countryNameClient;
        this.countryTransformer = countryTransformer;
        this.countryRepository = countryRepository;
    }

    public ResponseEntity getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        if (CollectionUtils.isEmpty(countries)) {
            GroupKTResponse<List<GroupKTCountry>> groupKTResponse = countryNameClient.getAllCountries();
            GroupKTRestResponse<List<GroupKTCountry>> restResponse = GroupKTHelper.getRestResponse(groupKTResponse);
            List<GroupKTCountry> groupKTCountries = GroupKTHelper.getResult(restResponse);
            countries = countryTransformer.transformList(groupKTCountries);
            countryRepository.saveAll(countries);
        }
        return ResponseEntityHelper.prepareResponseEntityForList(countries);
    }

    public ResponseEntity getCountryByCode(String code) {
        code = StringUtils.upperCase(code);
        Country country;
        switch (code.length()) {
            case 2:
                country = getCountryByCountryIsoCode2(code);
                break;
            case 3:
                country = getCountryByCountryIsoCode3(code);
                break;
            default:
                return ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_TRY_WITH_ISO_2_CODE_OR_ISO_3_CODE), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

    public ResponseEntity saveCountry(Country country) {
        Country countryEntity = countryRepository.findByShortName3(country.getShortName3());
        StringBuilder messageValueStringBuilder = new StringBuilder(MESSAGE_VALUE_COUNTRY_ALREADY_SAVE);
        if (countryEntity == null) {
            countryRepository.save(country);
            messageValueStringBuilder = new StringBuilder(MESSAGE_VALUE_COUNTRY_SAVED);
        }
        return ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(messageValueStringBuilder.toString()), HttpStatus.OK);
    }

    private Country getCountryByCountryIsoCode2(String code) {
        Country country = countryRepository.findByShortName2(code);
        if (country == null) {
            GroupKTResponse<GroupKTCountry> groupKTResponse = countryNameClient.getCountryByIso2Code(code);
            country = getCountryAndSaveOnDatabase(groupKTResponse);
        }
        return country;
    }

    private Country getCountryByCountryIsoCode3(String code) {
        Country country = countryRepository.findByShortName3(code);
        if (country == null) {
            GroupKTResponse<GroupKTCountry> groupKTResponse = countryNameClient.getCountryByIso3Code(code);
            country = getCountryAndSaveOnDatabase(groupKTResponse);
        }
        return country;
    }

    private Country getCountryAndSaveOnDatabase(GroupKTResponse<GroupKTCountry> groupKTResponse) {
        Country country;
        GroupKTRestResponse<GroupKTCountry> restResponse = GroupKTHelper.getRestResponse(groupKTResponse);
        GroupKTCountry groupKTCountry = GroupKTHelper.getResult(restResponse);
        country = countryTransformer.transform(groupKTCountry);
        countryRepository.save(country);
        return country;
    }

}
