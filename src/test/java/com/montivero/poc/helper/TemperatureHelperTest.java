package com.montivero.poc.helper;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TemperatureHelperTest {

    @Test
    public void shouldGetCelsiusTemperatureZeroFromFahrenheitIsNull() {
        assertThat(TemperatureHelper.fahrenheitToCelsius(null), is(0));
    }

    @Test
    public void shouldGetCelsiusTemperatureFromFahrenheit() {
        assertThat(TemperatureHelper.fahrenheitToCelsius(86), is(30));
    }

}