package com.montivero.poc.resource.domain.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.montivero.poc.resource.domain.persistence.LongIdentifiable;
import lombok.*;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "WEATHERS")
public class Weather extends LongIdentifiable {

    @Column(name = "CITY")
    private String city;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "STATE")
    private String state;

    @Column(name = "CREATED_REPORT")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdReport;

    @OneToOne(cascade = CascadeType.ALL)
    private TodayCondition todayCondition;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ForecastCondition> forecastConditions;

}
