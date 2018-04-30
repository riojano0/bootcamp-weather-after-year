package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.yahooQuery.Condition;
import com.montivero.poc.helper.LocalDateHelper;
import com.montivero.poc.helper.TemperatureHelper;
import com.montivero.poc.resource.domain.weather.TodayCondition;
import org.springframework.stereotype.Component;

@Component
public class TodayConditionTransformer extends BaseTransformer<Condition, TodayCondition> {

    @Override
    public TodayCondition transform(Condition condition) {
        TodayCondition todayCondition = new TodayCondition();
        if (condition != null) {
            todayCondition = new TodayCondition(
                    LocalDateHelper.getLocalDateFromConditionDate(condition.getDate()),
                    condition.getText(), TemperatureHelper.fahrenheitToCelsius(condition.getTemp()));
        }
        return todayCondition;
    }

}
