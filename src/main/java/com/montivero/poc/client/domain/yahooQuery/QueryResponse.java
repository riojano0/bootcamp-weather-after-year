package com.montivero.poc.client.domain.yahooQuery;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class QueryResponse {

    private Integer count;
    private String created;
    private String lang;
    @JsonProperty("results")
    private QueryResult queryResults;


}
