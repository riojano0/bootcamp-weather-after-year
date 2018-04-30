package com.montivero.poc.delegate;

import com.montivero.poc.client.YahooQueryLanguageClient;
import com.montivero.poc.client.domain.yahooQuery.*;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.repository.WeatherRepository;
import com.montivero.poc.resource.domain.weather.ForecastCondition;
import com.montivero.poc.resource.domain.weather.TodayCondition;
import com.montivero.poc.resource.domain.weather.Weather;
import com.montivero.poc.transformer.WeatherTransformer;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ResponseEntityHelper.class})
public class WeatherDelegateTest {

    private static final int FAHRENHEIT_HIGH_TEMPERATURE = 86;
    private static final int FAHRENHEIT_LOW_TEMPERATURE = 32;
    private static final int MAX_CELSIUS_TEMPERATURE = 30;
    private static final int MIN_CELSIUS_TEMPERATURE = 0;
    private static final String WEATHER_DESCRIPTION_CLOUDY = "Cloudy";
    private static final String COUNTRY_NAME = "Scadrial";
    private static final String CITY_NAME = "Luthadel";
    private static final String REGION_NAME = "Central Dominance";
    private static final String QUERY = String.format("select location,wind,atmosphere,item.condition,item from weather.forecast where woeid in (select woeid from geo.places(1) WHERE text=\"%s, %s\")", COUNTRY_NAME, CITY_NAME);
    private static final String QUERY_RESPONSE_FORMAT = "json";
    private static final LocalDate LOCAL_DATE_FORECAST_DAY_ONE = LocalDate.parse("2018-05-01");
    private static final LocalDate LOCAL_DATE_FORECAST_DAY_TWO = LocalDate.parse("2018-05-02");
    private static final LocalDateTime LOCAL_DATE_TIME_CREATED_REPORT = LocalDateTime.now();

    private YahooQuery yahooQuery;
    private Weather weather;

    @Mock
    private YahooQueryLanguageClient mockYahooQueryLanguageClient;
    @Mock
    private WeatherTransformer mockWeatherTransformer;
    @Mock
    private WeatherRepository mockWeatherRepository;

    private WeatherDelegate weatherDelegate;

    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.mockStatic(ResponseEntityHelper.class);
        weatherDelegate = new WeatherDelegate(mockYahooQueryLanguageClient, mockWeatherTransformer, mockWeatherRepository);
        QueryResult queryResult = new QueryResult();
        queryResult.setChannelResponse(createChannelResponse());
        QueryResponse queryResponse = new QueryResponse();
        queryResponse.setQueryResults(queryResult);
        yahooQuery = new YahooQuery();
        yahooQuery.setQueryResponse(queryResponse);
        TodayCondition todayCondition = new TodayCondition();
        todayCondition.setDate(LOCAL_DATE_FORECAST_DAY_ONE);
        todayCondition.setCurrentTemp(MAX_CELSIUS_TEMPERATURE);
        todayCondition.setConditionDescription(WEATHER_DESCRIPTION_CLOUDY);
        ForecastCondition forecastCondition = new ForecastCondition();
        forecastCondition.setDate(LOCAL_DATE_FORECAST_DAY_TWO);
        forecastCondition.setMaxTemp(MAX_CELSIUS_TEMPERATURE);
        forecastCondition.setMinTemp(MIN_CELSIUS_TEMPERATURE);
        forecastCondition.setConditionDescription(WEATHER_DESCRIPTION_CLOUDY);
        Set<ForecastCondition> forecastConditionTreeSet = new TreeSet<>();
        forecastConditionTreeSet.add(forecastCondition);
        weather = Weather.builder()
                .city(CITY_NAME)
                .country(COUNTRY_NAME)
                .state(REGION_NAME)
                .createdReport(LOCAL_DATE_TIME_CREATED_REPORT)
                .todayCondition(todayCondition)
                .forecastConditions(forecastConditionTreeSet)
                .build();
        when(ResponseEntityHelper.prepareResponseEntityForWeather(weather)).thenReturn(ResponseEntity.ok(weather));
    }

    @Test
    public void shouldGetWeatherFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResults() {
        when(mockWeatherRepository.findByCountryAndCity(COUNTRY_NAME, CITY_NAME)).thenReturn(null);
        when(mockYahooQueryLanguageClient.getQuery(QUERY, QUERY_RESPONSE_FORMAT)).thenReturn(yahooQuery);
        when(mockWeatherTransformer.transform(yahooQuery.getQueryResponse().getQueryResults().getChannelResponse())).thenReturn(weather);

        ResponseEntity responseEntity = weatherDelegate.getWeatherByCountryAndCity(COUNTRY_NAME, CITY_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Weather weather = (Weather) responseEntity.getBody();
        assertThat(weather.getCity(), is(CITY_NAME));

        verify(mockWeatherRepository).findByCountryAndCity(COUNTRY_NAME, CITY_NAME);
        verify(mockYahooQueryLanguageClient).getQuery(QUERY, QUERY_RESPONSE_FORMAT);
        verify(mockWeatherTransformer).transform(yahooQuery.getQueryResponse().getQueryResults().getChannelResponse());
        verify(mockWeatherRepository).save(weather);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForWeather(weather);
    }

    @Test
    public void shouldGetWeatherFromClientWhenDatabaseHaveMatchButHaveOneHourOfDifferenceFromCreatedReportAndAfterCallDatabaseForSaveTheResults() {
        weather.setCreatedReport(LOCAL_DATE_TIME_CREATED_REPORT.minusMinutes(61));
        when(mockWeatherRepository.findByCountryAndCity(COUNTRY_NAME, CITY_NAME)).thenReturn(weather);
        when(mockYahooQueryLanguageClient.getQuery(QUERY, QUERY_RESPONSE_FORMAT)).thenReturn(yahooQuery);
        when(mockWeatherTransformer.transform(yahooQuery.getQueryResponse().getQueryResults().getChannelResponse())).thenReturn(weather);

        ResponseEntity responseEntity = weatherDelegate.getWeatherByCountryAndCity(COUNTRY_NAME, CITY_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Weather weather = (Weather) responseEntity.getBody();
        assertThat(weather.getCity(), is(CITY_NAME));

        verify(mockWeatherRepository).findByCountryAndCity(COUNTRY_NAME, CITY_NAME);
        verify(mockYahooQueryLanguageClient).getQuery(QUERY, QUERY_RESPONSE_FORMAT);
        verify(mockWeatherTransformer).transform(yahooQuery.getQueryResponse().getQueryResults().getChannelResponse());
        verify(mockWeatherRepository).save(weather);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForWeather(weather);
    }

    @Test
    public void shouldGetWeatherFromDatabaseWhenHaveMatch() {
        when(mockWeatherRepository.findByCountryAndCity(COUNTRY_NAME, CITY_NAME)).thenReturn(weather);

        ResponseEntity responseEntity = weatherDelegate.getWeatherByCountryAndCity(COUNTRY_NAME, CITY_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        Weather weather = (Weather) responseEntity.getBody();
        assertThat(weather.getCity(), is(CITY_NAME));

        verify(mockWeatherRepository).findByCountryAndCity(COUNTRY_NAME, CITY_NAME);
        verify(mockYahooQueryLanguageClient, never()).getQuery(QUERY, QUERY_RESPONSE_FORMAT);
        verify(mockWeatherTransformer, never()).transform(yahooQuery.getQueryResponse().getQueryResults().getChannelResponse());
        verify(mockWeatherRepository, never()).save(weather);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForWeather(weather);
    }

    private ChannelResponse createChannelResponse() {
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
        location.setCity(CITY_NAME);
        location.setRegion(REGION_NAME);
        location.setCountry(COUNTRY_NAME);
        ChannelResponse channelResponse = new ChannelResponse();
        channelResponse.setItem(item);
        channelResponse.setLocation(location);
        return channelResponse;
    }

}