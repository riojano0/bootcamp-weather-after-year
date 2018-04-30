package com.montivero.poc.client.domain.yahooQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelResponse {

    private Location location;
    private Wind wind;
    private Atmosphere atmosphere;
    private Item item;

}
