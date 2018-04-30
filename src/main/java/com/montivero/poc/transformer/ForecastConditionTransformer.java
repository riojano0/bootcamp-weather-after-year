package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.yahooQuery.Forecast;
import com.montivero.poc.helper.TemperatureHelper;
import com.montivero.poc.resource.domain.weather.ForecastCondition;
import org.springframework.stereotype.Component;

@Component
public class ForecastConditionTransformer extends BaseTransformer<Forecast, ForecastCondition> {

    @Override
    public ForecastCondition transform(Forecast forecast) {
        ForecastCondition forecastCondition = new ForecastCondition();
        if (forecast != null) {
            forecastCondition = new ForecastCondition(
                    forecast.getDate(),
                    forecast.getText(),
                    TemperatureHelper.fahrenheitToCelsius(forecast.getHigh()),
                    TemperatureHelper.fahrenheitToCelsius(forecast.getLow()));
        }
        return forecastCondition;
    }

}
