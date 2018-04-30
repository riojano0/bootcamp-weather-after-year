package com.montivero.poc.resource.domain.weather;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "FORECAST_CONDITIONS")
public class ForecastCondition extends Condition {

    @Column(name = "MAX_TEMP")
    private Integer maxTemp;
    @Column(name = "MIN_TEMP")
    private Integer minTemp;

    public ForecastCondition(LocalDate date, String conditionDescription, Integer maxTemp, Integer minTemp) {
        super(date, conditionDescription);
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }

}
