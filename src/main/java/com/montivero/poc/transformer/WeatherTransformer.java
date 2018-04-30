package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.yahooQuery.*;
import com.montivero.poc.resource.domain.weather.ForecastCondition;
import com.montivero.poc.resource.domain.weather.TodayCondition;
import com.montivero.poc.resource.domain.weather.Weather;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.TreeSet;

@Component
public class WeatherTransformer extends BaseTransformer<ChannelResponse, Weather> {

    private final TodayConditionTransformer todayConditionTransformer;
    private final ForecastConditionTransformer forecastConditionTransformer;

    @Autowired
    public WeatherTransformer(TodayConditionTransformer todayConditionTransformer, ForecastConditionTransformer forecastConditionTransformer) {
        this.todayConditionTransformer = todayConditionTransformer;
        this.forecastConditionTransformer = forecastConditionTransformer;
    }

    @Override
    public Weather transform(ChannelResponse channelResponse) {
        Location location = channelResponse.getLocation();
        Condition condition = null;
        TreeSet<Forecast> forecastTreeSet = null;
        Item item = channelResponse.getItem();
        if (item != null) {
            condition = item.getCondition();
            forecastTreeSet = item.getForecastList();
        }
        TodayCondition todayCondition = todayConditionTransformer.transform(condition);
        Collection<ForecastCondition> forecastConditions = CollectionUtils.collect(forecastTreeSet, forecastConditionTransformer);
        TreeSet<ForecastCondition> treeSet = new TreeSet<>(forecastConditions);

        return Weather.builder()
                .city(location.getCity())
                .country(location.getCountry())
                .state(location.getRegion())
                .todayCondition(todayCondition)
                .forecastConditions(treeSet)
                .createdReport(LocalDateTime.now())
                .build();
    }

}
