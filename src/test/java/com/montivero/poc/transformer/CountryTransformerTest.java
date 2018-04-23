package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.GroupKTCountry;
import com.montivero.poc.resource.domain.Country;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CountryTransformerTest {

    private GroupKTCountry groupKTCountry;
    private CountryTransformer countryTransformer;

    @Before
    public void setUp() {
        countryTransformer = new CountryTransformer();
        groupKTCountry = new GroupKTCountry();
        groupKTCountry.setName("Argentina");
        groupKTCountry.setAlpha2_code("AR");
        groupKTCountry.setAlpha3_code("ARG");
    }

    @Test
    public void shouldTransformGroupKTCountryToNewCountryWhenIsNull() {

        Country country = countryTransformer.transform(null);

        assertThat(country.getName(), nullValue());
        assertThat(country.getShortName2(), nullValue());
        assertThat(country.getShortName3(), nullValue());
    }

    @Test
    public void shouldTransformGroupKTCountryToCountryWhenIsNotNull() {

        Country country = countryTransformer.transform(groupKTCountry);

        assertThat(country.getName(), is("Argentina"));
        assertThat(country.getShortName2(), is("AR"));
        assertThat(country.getShortName3(), is("ARG"));
    }

    @Test
    public void shouldTransformGroupKTCountryListToCountryListWhenIsNotEmpty() {

        List<Country> countryList = countryTransformer.transformList(Collections.singletonList(groupKTCountry));

        assertThat(countryList, hasSize(1));
        assertThat(countryList.get(0).getName(), is("Argentina"));
        assertThat(countryList.get(0).getShortName2(), is("AR"));
        assertThat(countryList.get(0).getShortName3(), is("ARG"));
    }

    @Test
    public void shouldTransformGroupKTCountryListToCountryListEmptyWhenIsNull() {

        List<Country> countryList = countryTransformer.transformList(null);

        assertThat(countryList, hasSize(0));
    }

    @Test
    public void shouldTransformGroupKTCountryListToCountryListEmptyWhenIsEmpty() {

        List<Country> countryList = countryTransformer.transformList(new ArrayList<>());

        assertThat(countryList, hasSize(0));
    }

}