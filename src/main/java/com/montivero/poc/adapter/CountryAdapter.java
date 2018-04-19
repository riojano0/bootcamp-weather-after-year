package com.montivero.poc.adapter;

import com.montivero.poc.client.domain.GroupKTCountry;
import com.montivero.poc.resource.domain.Country;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CountryAdapter {


    public List<Country> adaptGroupKTCountriesToCountries(List<GroupKTCountry> groupKTCountries) {
        List<Country> countries = new ArrayList<>();
        for (GroupKTCountry groupKTCountry : CollectionUtils.emptyIfNull(groupKTCountries)) {
            countries.add(adaptGroupKTCountryToCountry(groupKTCountry));
        }
        return countries;
    }

    public Country adaptGroupKTCountryToCountry(GroupKTCountry groupKTCountry) {
        Country country = new Country();
        if (groupKTCountry != null) {
            country = Country.builder()
                    .name(groupKTCountry.getName())
                    .shortName2(groupKTCountry.getAlpha2_code())
                    .shortName3(groupKTCountry.getAlpha3_code())
                    .build();
        }
        return country;
    }

}
