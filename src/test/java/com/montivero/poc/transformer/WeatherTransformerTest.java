package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.yahooQuery.*;
import com.montivero.poc.resource.domain.weather.ForecastCondition;
import com.montivero.poc.resource.domain.weather.TodayCondition;
import com.montivero.poc.resource.domain.weather.Weather;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Spy;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class WeatherTransformerTest {

    private static final int FAHRENHEIT_HIGH_TEMPERATURE = 86;
    private static final int FAHRENHEIT_LOW_TEMPERATURE = 32;
    private static final String WEATHER_DESCRIPTION_CLOUDY = "Cloudy";
    private static final String CITY_NAME_VIEDMA = "Viedma";
    private static final String REGION_NAME_RIO_NEGRO = "Rio Negro";
    private static final String COUNTRY_NAME_ARGENTINA = "Argentina";
    private static final LocalDate LOCAL_DATE_FORECAST_DAY_ONE = LocalDate.parse("2018-05-01");
    private static final LocalDate LOCAL_DATE_FORECAST_DAY_TWO = LocalDate.parse("2018-05-02");
    private static final int MAX_CELSIUS_TEMPERATURE = 30;
    private static final int MIN_CELSIUS_TEMPERATURE = 0;

    private ChannelResponse channelResponse;
    private Weather weather;

    @Spy
    private TodayConditionTransformer mockTodayConditionTransformer;
    @Spy
    private ForecastConditionTransformer mockForecastConditionTransformer;

    private WeatherTransformer weatherTransformer;

    @Before
    public void setUp() {
        initMocks(this);
        weatherTransformer = new WeatherTransformer(mockTodayConditionTransformer, mockForecastConditionTransformer);

        Condition yahooCondition = new Condition();
        yahooCondition.setDate("Mon, 30 Apr 2018 10:00 AM ART");
        yahooCondition.setTemp(FAHRENHEIT_HIGH_TEMPERATURE);
        yahooCondition.setText(WEATHER_DESCRIPTION_CLOUDY);
        Forecast forecastDayOne = new Forecast();
        forecastDayOne.setDate(LOCAL_DATE_FORECAST_DAY_ONE);
        forecastDayOne.setHigh(FAHRENHEIT_HIGH_TEMPERATURE);
        forecastDayOne.setLow(FAHRENHEIT_LOW_TEMPERATURE);
        forecastDayOne.setText(WEATHER_DESCRIPTION_CLOUDY);
        Forecast forecastDayTwo = new Forecast();
        forecastDayTwo.setDate(LOCAL_DATE_FORECAST_DAY_TWO);
        forecastDayTwo.setHigh(FAHRENHEIT_HIGH_TEMPERATURE);
        forecastDayTwo.setLow(FAHRENHEIT_LOW_TEMPERATURE);
        forecastDayTwo.setText(WEATHER_DESCRIPTION_CLOUDY);
        TreeSet<Forecast> forecastTreeSet = new TreeSet<>();
        forecastTreeSet.add(forecastDayTwo);
        forecastTreeSet.add(forecastDayOne);
        Item item = new Item();
        item.setCondition(yahooCondition);
        item.setForecastList(forecastTreeSet);
        Location location = new Location();
        location.setCity(CITY_NAME_VIEDMA);
        location.setRegion(REGION_NAME_RIO_NEGRO);
        location.setCountry(COUNTRY_NAME_ARGENTINA);
        channelResponse = new ChannelResponse();
        channelResponse.setItem(item);
        channelResponse.setLocation(location);
    }

    @Test
    public void shouldTransformChannelResponseToWeather() {
        weather = weatherTransformer.transform(channelResponse);

        assertThat(weather.getCity(), is(CITY_NAME_VIEDMA));
        assertThat(weather.getCountry(), is(COUNTRY_NAME_ARGENTINA));
        assertThat(weather.getState(), is(REGION_NAME_RIO_NEGRO));
        assertThat(weather.getCreatedReport().toLocalDate(), is(LocalDate.now()));

        TodayCondition todayCondition = weather.getTodayCondition();
        assertThat(todayCondition.getDate(), is(LocalDate.now()));
        assertThat(todayCondition.getConditionDescription(), is(WEATHER_DESCRIPTION_CLOUDY));
        assertThat(todayCondition.getCurrentTemp(), is(MAX_CELSIUS_TEMPERATURE));

        Set<ForecastCondition> forecastConditions = weather.getForecastConditions();
        assertThat(forecastConditions, hasSize(2));
        Iterator<ForecastCondition> forecastConditionIterator = forecastConditions.iterator();
        ForecastCondition forecastConditionDayOne = forecastConditionIterator.next();
        assertThat(forecastConditionDayOne.getDate(), is(LOCAL_DATE_FORECAST_DAY_ONE));
        assertThat(forecastConditionDayOne.getMaxTemp(), is(MAX_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionDayOne.getMinTemp(), is(MIN_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionDayOne.getConditionDescription(), is(WEATHER_DESCRIPTION_CLOUDY));
        ForecastCondition forecastConditionDayTwo = forecastConditionIterator.next();
        assertThat(forecastConditionDayTwo.getDate(), is(LOCAL_DATE_FORECAST_DAY_TWO));
        assertThat(forecastConditionDayTwo.getMaxTemp(), is(MAX_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionDayTwo.getMinTemp(), is(MIN_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionDayTwo.getConditionDescription(), is(WEATHER_DESCRIPTION_CLOUDY));

        verify(mockTodayConditionTransformer).transform(channelResponse.getItem().getCondition());
        verify(mockForecastConditionTransformer).transform(channelResponse.getItem().getForecastList().first());
    }

    @Test
    public void shouldTransformChannelResponseToWeatherWhenItemIsNull() {
        channelResponse.setItem(null);

        weather = weatherTransformer.transform(channelResponse);

        assertThat(weather.getCity(), is(CITY_NAME_VIEDMA));
        assertThat(weather.getCountry(), is(COUNTRY_NAME_ARGENTINA));
        assertThat(weather.getState(), is(REGION_NAME_RIO_NEGRO));
        assertThat(weather.getCreatedReport().toLocalDate(), is(LocalDate.now()));

        TodayCondition todayCondition = weather.getTodayCondition();
        assertThat(todayCondition, notNullValue());

        Set<ForecastCondition> forecastConditions = weather.getForecastConditions();
        assertThat(forecastConditions, hasSize(0));

        verify(mockTodayConditionTransformer).transform(null);
        verifyZeroInteractions(mockForecastConditionTransformer);
    }

}