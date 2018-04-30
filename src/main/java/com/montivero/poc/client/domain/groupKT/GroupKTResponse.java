package com.montivero.poc.client.domain.groupKT;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupKTResponse<T> {

    @JsonProperty("RestResponse")
    private GroupKTRestResponse<T> restResponse;

}
