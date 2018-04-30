package com.montivero.poc.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TemperatureHelper {

    public static Integer fahrenheitToCelsius(Integer fahrenheit) {
        return fahrenheit != null
                ? (fahrenheit - 32) * 5 / 9
                : 0;
    }

}
