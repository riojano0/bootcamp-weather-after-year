package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.groupKT.GroupKTCountry;
import com.montivero.poc.resource.domain.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryTransformer extends BaseTransformer<GroupKTCountry, Country> {

    @Override
    public Country transform(GroupKTCountry groupKTCountry) {
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
