package com.montivero.poc.resource;

import com.montivero.poc.delegate.CountryDelegate;
import com.montivero.poc.resource.domain.Country;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CountryResourceTest {

    private static final String COUNTRY_CODE = "countryCode";
    private ResponseEntity responseEntitySuccessful;

    @Mock
    private CountryDelegate mockCountryDelegate;

    private CountryResource countryResource;

    @Before
    public void setUp() {
        initMocks(this);
        countryResource = new CountryResource(mockCountryDelegate);

        responseEntitySuccessful = new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    @Test
    public void shouldGetAllCountries() {
        when(mockCountryDelegate.getAllCountries()).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryResource.getAllCountries();

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryDelegate).getAllCountries();
    }

    @Test
    public void shouldGetCountryByCode() {
        when(mockCountryDelegate.getCountryByCode(COUNTRY_CODE)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryResource.getCountryByCode(COUNTRY_CODE);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryDelegate).getCountryByCode(COUNTRY_CODE);
    }

    @Test
    public void shouldSaveCountry() {
        Country country = new Country();
        when(mockCountryDelegate.saveCountry(country)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryResource.saveCountry(country);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryDelegate).saveCountry(country);
    }

}