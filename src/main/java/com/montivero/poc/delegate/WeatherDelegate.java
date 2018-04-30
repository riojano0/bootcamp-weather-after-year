package com.montivero.poc.delegate;

import com.montivero.poc.client.YahooQueryLanguageClient;
import com.montivero.poc.client.domain.yahooQuery.QueryResponse;
import com.montivero.poc.client.domain.yahooQuery.QueryResult;
import com.montivero.poc.client.domain.yahooQuery.YahooQuery;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.repository.WeatherRepository;
import com.montivero.poc.resource.domain.weather.Weather;
import com.montivero.poc.transformer.WeatherTransformer;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class WeatherDelegate {

    private static final String RAW_QUERY = "select location,wind,atmosphere,item.condition,item from weather.forecast where woeid in (select woeid from geo.places(1) WHERE text=\"%s, %s\")";
    private static final String QUERY_RESPONSE_FORMAT = "json";

    private final YahooQueryLanguageClient yahooQueryLanguageClient;
    private final WeatherTransformer weatherTransformer;
    private final WeatherRepository weatherRepository;

    @Autowired
    public WeatherDelegate(YahooQueryLanguageClient yahooQueryLanguageClient, WeatherTransformer weatherTransformer,
                           WeatherRepository weatherRepository) {
        this.yahooQueryLanguageClient = yahooQueryLanguageClient;
        this.weatherTransformer = weatherTransformer;
        this.weatherRepository = weatherRepository;
    }

    public ResponseEntity getWeatherByCountryAndCity(String country, String city) {
        Weather weather = weatherRepository.findByCountryAndCity(country, city);
        if (weather == null || hasMoreThanOneHourTheRequest(weather.getCreatedReport())) {
            String formatQuery = String.format(RAW_QUERY, country, city);
            YahooQuery yahooQuery = yahooQueryLanguageClient.getQuery(formatQuery, QUERY_RESPONSE_FORMAT);
            QueryResponse queryResponse = getQueryResponse(yahooQuery);
            QueryResult queryResult = getQueryResult(queryResponse);
            weather = weatherTransformer.transform(queryResult.getChannelResponse());
            weatherRepository.save(weather);
        }
        return ResponseEntityHelper.prepareResponseEntityForWeather(weather);
    }

    private boolean hasMoreThanOneHourTheRequest(LocalDateTime createdReport) {
        return Minutes.minutesBetween(createdReport, LocalDateTime.now()).isGreaterThan(Hours.ONE.toStandardMinutes());
    }

    private QueryResponse getQueryResponse(YahooQuery yahooQuery) {
        QueryResponse queryResponse = yahooQuery.getQueryResponse();
        return queryResponse != null
                ? queryResponse
                : new QueryResponse();
    }

    private QueryResult getQueryResult(QueryResponse queryResponse) {
        QueryResult queryResults = queryResponse.getQueryResults();
        return queryResults != null
                ? queryResults
                : new QueryResult();
    }

}

