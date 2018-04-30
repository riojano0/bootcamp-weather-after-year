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
@Table(name = "TODAY_CONDITIONS")
public class TodayCondition extends Condition {

    @Column(name = "CURRENT_TEMP")
    private Integer currentTemp;

    public TodayCondition(LocalDate date, String conditionDescription, Integer currentTemp) {
        super(date, conditionDescription);
        this.currentTemp = currentTemp;
    }
}
