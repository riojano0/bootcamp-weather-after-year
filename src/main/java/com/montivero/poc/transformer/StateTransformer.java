package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.GroupKTState;
import com.montivero.poc.resource.domain.State;
import org.springframework.stereotype.Component;

@Component
public class StateTransformer extends BaseTransformer<GroupKTState, State> {

    @Override
    public State transform(GroupKTState groupKTState) {
        State state = new State();
        if (groupKTState != null) {
            state = State.builder()
                    .name(groupKTState.getName())
                    .shortName(groupKTState.getAbbreviateName())
                    .capital(groupKTState.getCapital())
                    .area(groupKTState.getArea())
                    .largestCity(groupKTState.getLargestCity())
                    .area(groupKTState.getArea())
                    .countryShortName3(groupKTState.getCountry())
                    .build();
        }
        return state;
    }

}
