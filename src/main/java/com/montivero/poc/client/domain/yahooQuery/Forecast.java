package com.montivero.poc.client.domain.yahooQuery;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Forecast implements Comparable<Forecast> {

    private Integer code;
    @JsonFormat(pattern = "dd MMM yyyy", locale = "en_US", shape = JsonFormat.Shape.STRING)
    private LocalDate date;
    private String day;
    private Integer high;
    private Integer low;
    private String text;

    @Override
    public int compareTo(Forecast otherForecast) {
        return this.getDate().compareTo(otherForecast.getDate());
    }

}
