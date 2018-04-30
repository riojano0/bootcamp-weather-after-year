package com.montivero.poc.transformer;


import com.montivero.poc.client.domain.yahooQuery.Condition;
import com.montivero.poc.helper.LocalDateHelper;
import com.montivero.poc.helper.TemperatureHelper;
import com.montivero.poc.resource.domain.weather.TodayCondition;
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
@PrepareForTest({TemperatureHelper.class, LocalDateHelper.class})
public class TodayConditionTransformerTest {

    private static final String YAHOO_CONDITION_DATE = "Mon, 30 Apr 2018 10:00 AM ART";
    private static final LocalDate localDate = LocalDate.parse("2018-04-30");
    private static final int FAHRENHEIT_TEMPERATURE = 86;
    private static final int CELSIUS_TEMPERATURE = 30;
    private static final String CLOUDY_TEXT = "Cloudy";

    private Condition yahooCondition;
    private TodayCondition todayCondition;
    private List<TodayCondition> todayConditionList;
    private TodayConditionTransformer todayConditionTransformer;

    @Before
    public void setUp() {
        todayConditionTransformer = new TodayConditionTransformer();

        PowerMockito.mockStatic(TemperatureHelper.class);
        PowerMockito.mockStatic(LocalDateHelper.class);
        yahooCondition = new Condition();
        yahooCondition.setTemp(FAHRENHEIT_TEMPERATURE);
        yahooCondition.setDate(YAHOO_CONDITION_DATE);
        yahooCondition.setText(CLOUDY_TEXT);

        when(TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE)).thenReturn(CELSIUS_TEMPERATURE);
        when(LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE)).thenReturn(localDate);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformConditionToNewTodayConditionWhenIsNull() {
        todayCondition = todayConditionTransformer.transform(null);

        assertThat(todayCondition, notNullValue());

        verifyStatic(never());
        TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE);
        verifyStatic(never());
        LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformConditionToNewTodayConditionWhenIsNotNull() {
        todayCondition = todayConditionTransformer.transform(yahooCondition);

        assertThat(todayCondition.getDate(), is(localDate));
        assertThat(todayCondition.getCurrentTemp(), is(CELSIUS_TEMPERATURE));
        assertThat(todayCondition.getConditionDescription(), is(CLOUDY_TEXT));

        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE);
        verifyStatic(times(1));
        LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformConditionListToTodayConditionListWhenIsNotEmpty() {
        todayConditionList = todayConditionTransformer.transformList(Collections.singletonList(yahooCondition));

        assertThat(todayConditionList, hasSize(1));
        assertThat(todayConditionList.get(0).getDate(), is(localDate));
        assertThat(todayConditionList.get(0).getCurrentTemp(), is(CELSIUS_TEMPERATURE));
        assertThat(todayConditionList.get(0).getConditionDescription(), is(CLOUDY_TEXT));

        verifyStatic(times(1));
        TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE);
        verifyStatic(times(1));
        LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformConditionListToNewTodayConditionListWhenIsNull() {
        todayConditionList = todayConditionTransformer.transformList(null);

        assertThat(todayConditionList, hasSize(0));

        verifyStatic(never());
        TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE);
        verifyStatic(never());
        LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE);
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void shouldTransformConditionListToNewTodayConditionListWhenIsEmpty() {
        todayConditionList = todayConditionTransformer.transformList(new ArrayList<>());

        assertThat(todayConditionList, hasSize(0));

        verifyStatic(never());
        TemperatureHelper.fahrenheitToCelsius(FAHRENHEIT_TEMPERATURE);
        verifyStatic(never());
        LocalDateHelper.getLocalDateFromConditionDate(YAHOO_CONDITION_DATE);
    }

}