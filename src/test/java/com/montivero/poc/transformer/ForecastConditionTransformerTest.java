package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.yahooQuery.Forecast;
import com.montivero.poc.helper.TemperatureHelper;
import com.montivero.poc.resource.domain.weather.ForecastCondition;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TemperatureHelper.class)
public class ForecastConditionTransformerTest {

    private static final int LOW_FAHRENHEIT_TEMPERATURE = 32;
    private static final int MAX_FAHRENHEIT_TEMPERATURE = 86;
    private static final int MAX_CELSIUS_TEMPERATURE = 30;
    private static final int LOW_CELSIUS_TEMPERATURE = 0;
    private static final LocalDate TODAY = LocalDate.now();

    private Forecast forecast;
    private ForecastCondition forecastCondition;
    private List<ForecastCondition> forecastConditionList;
    private ForecastConditionTransformer forecastConditionTransformer;

    @Before
    public void setUp() {
        forecastConditionTransformer = new ForecastConditionTransformer();

        PowerMockito.mockStatic(TemperatureHelper.class);
        forecast = new Forecast();
        forecast.setDate(TODAY);
        forecast.setDay("Sunday");
        forecast.setText("Cloudy");
        forecast.setHigh(MAX_FAHRENHEIT_TEMPERATURE);
        forecast.setLow(LOW_FAHRENHEIT_TEMPERATURE);
        when(TemperatureHelper.fahrenheitToCelsius(MAX_FAHRENHEIT_TEMPERATURE)).thenReturn(MAX_CELSIUS_TEMPERATURE);
        when(TemperatureHelper.fahrenheitToCelsius(LOW_FAHRENHEIT_TEMPERATURE)).thenReturn(LOW_CELSIUS_TEMPERATURE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformForecastToNewForecastConditionWhenIsNull() {
        forecastCondition = forecastConditionTransformer.transform(null);

        assertThat(forecastCondition.getDate(), nullValue());
        assertThat(forecastCondition.getMaxTemp(), nullValue());
        assertThat(forecastCondition.getMinTemp(), nullValue());
        assertThat(forecastCondition.getConditionDescription(), nullValue());

        verifyStatic(never());
        TemperatureHelper.fahrenheitToCelsius(MAX_FAHRENHEIT_TEMPERATURE);
        verifyStatic(never());
        TemperatureHelper.fahrenheitToCelsius(LOW_FAHRENHEIT_TEMPERATURE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformForecastToNewForecastConditionWhenIsNotNull() {
        forecastCondition = forecastConditionTransformer.transform(forecast);

        assertThat(forecastCondition.getDate(), is(TODAY));
        assertThat(forecastCondition.getMaxTemp(), is(MAX_CELSIUS_TEMPERATURE));
        assertThat(forecastCondition.getMinTemp(), is(LOW_CELSIUS_TEMPERATURE));
        assertThat(forecastCondition.getConditionDescription(), is("Cloudy"));

        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(MAX_FAHRENHEIT_TEMPERATURE);
        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(LOW_FAHRENHEIT_TEMPERATURE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformForecastListToNewForecastConditionListWhenIsNotEmpty() {
        forecastConditionList = forecastConditionTransformer.transformList(Collections.singletonList(forecast));

        assertThat(forecastConditionList, hasSize(1));
        assertThat(forecastConditionList.get(0).getDate(), is(TODAY));
        assertThat(forecastConditionList.get(0).getMaxTemp(), is(MAX_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionList.get(0).getMinTemp(), is(LOW_CELSIUS_TEMPERATURE));
        assertThat(forecastConditionList.get(0).getConditionDescription(), is("Cloudy"));

        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(MAX_FAHRENHEIT_TEMPERATURE);
        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(LOW_FAHRENHEIT_TEMPERATURE);
    }

    @Test
    public void shouldTransformForecastListToNewForecastConditionListWhenIsNull() {
        forecastConditionList = forecastConditionTransformer.transformList(null);

        assertThat(forecastConditionList, hasSize(0));
    }

    @Test
    public void shouldTransformForecastListToNewForecastConditionListWhenIsEmpty() {
        forecastConditionList = forecastConditionTransformer.transformList(new ArrayList<>());

        assertThat(forecastConditionList, hasSize(0));
    }

}