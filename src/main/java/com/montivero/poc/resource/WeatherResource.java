package com.montivero.poc.resource;

import com.montivero.poc.delegate.WeatherDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weather")
public class WeatherResource {

    private final WeatherDelegate weatherDelegate;

    @Autowired
    public WeatherResource(WeatherDelegate weatherDelegate) {
        this.weatherDelegate = weatherDelegate;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{country}/{city}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity getWeather(@PathVariable(value = "country") String country, @PathVariable(value = "city") String city) {
        return weatherDelegate.getWeatherByCountryAndCity(country, city);
    }

}
