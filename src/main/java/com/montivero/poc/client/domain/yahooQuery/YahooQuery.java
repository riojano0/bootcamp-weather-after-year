package com.montivero.poc.client.domain.yahooQuery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class YahooQuery {

    @JsonProperty("query")
    private QueryResponse queryResponse;

}
