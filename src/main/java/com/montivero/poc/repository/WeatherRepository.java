package com.montivero.poc.repository;

import com.montivero.poc.resource.domain.weather.Weather;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRepository extends CrudRepository<Weather, Long> {

    Weather findByCountryAndCity(String country, String city);

}
