package com.montivero.poc.resource;

import com.montivero.poc.delegate.WeatherDelegate;
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

public class WeatherResourceTest {

    private static final String COUNTRY_ARGENTINA = "Argentina";
    private static final String CITY_CORDOBA = "Cordoba";

    @Mock
    private WeatherDelegate mockWeatherDelegate;

    private ResponseEntity responseEntitySuccessful;
    private WeatherResource weatherResource;

    @Before
    public void setUp() {
        initMocks(this);
        weatherResource = new WeatherResource(mockWeatherDelegate);
        responseEntitySuccessful = new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    @Test
    public void shouldGetWeatherFromCityCountry() {
        when(mockWeatherDelegate.getWeatherByCountryAndCity(COUNTRY_ARGENTINA, CITY_CORDOBA)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = weatherResource.getWeather(COUNTRY_ARGENTINA, CITY_CORDOBA);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockWeatherDelegate).getWeatherByCountryAndCity(COUNTRY_ARGENTINA, CITY_CORDOBA);
    }
}