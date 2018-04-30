package com.montivero.poc.client.domain.groupKT;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupKTState {

    private String country;
    private String name;
    @JsonProperty("abbr")
    private String abbreviateName;
    private String area;
    @JsonProperty("largest_city")
    private String largestCity;
    private String capital;

}
