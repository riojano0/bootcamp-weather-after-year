package com.montivero.poc.client.domain.yahooQuery;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Atmosphere {

    private Integer humidity;
    private Float pressure;
    private Integer rising;
    private Float visibility;

}
