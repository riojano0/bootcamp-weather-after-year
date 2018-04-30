package com.montivero.poc.client.domain.groupKT;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class GroupKTRestResponse<T> {

    private List<String> messages;
    private T result;

}
