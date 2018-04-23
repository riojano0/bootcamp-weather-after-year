package com.montivero.poc.adapter;

import com.montivero.poc.client.domain.GroupKTState;
import com.montivero.poc.resource.domain.State;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StateAdapter {

    public List<State> adaptGroupKTCountriesToCountries(List<GroupKTState> groupKTStateList) {
        List<State> stateList = new ArrayList<>();
        for (GroupKTState groupKTCountry : CollectionUtils.emptyIfNull(groupKTStateList)) {
            stateList.add(adaptGroupKTStateToState(groupKTCountry));
        }
        return stateList;
    }

    public State adaptGroupKTStateToState(GroupKTState groupKTState) {
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
