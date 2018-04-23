package com.montivero.poc.delegate;

import com.montivero.poc.transformer.StateTransformer;
import com.montivero.poc.client.StateNameClient;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.client.domain.GroupKTState;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.resource.domain.State;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateDelegate {

    private final StateNameClient stateNameClient;
    private final StateTransformer stateTransformer;

    @Autowired
    public StateDelegate(StateNameClient stateNameClient, StateTransformer stateTransformer) {
        this.stateNameClient = stateNameClient;
        this.stateTransformer = stateTransformer;
    }

    public ResponseEntity getAllStatesByCountry(String country) {
        GroupKTResponse<List<GroupKTState>> groupKTResponse = stateNameClient.getAllStateByCountry(StringUtils.upperCase(country));
        GroupKTRestResponse<List<GroupKTState>> groupKTRestResponse = GroupKTHelper.getRestResponse(groupKTResponse);
        List<GroupKTState> groupKTStates = GroupKTHelper.getResult(groupKTRestResponse);
        List<State> states = stateTransformer.transformList(groupKTStates);
        return ResponseEntityHelper.prepareResponseEntityForList(states);
    }

    public ResponseEntity getStateByCountryAndShortName(String country, String shortStateName) {
        GroupKTResponse<GroupKTState> groupKTResponse = stateNameClient.getStateDetailsByCountryAndAbbrName(StringUtils.upperCase(country), StringUtils.upperCase(shortStateName));
        GroupKTRestResponse<GroupKTState> groupKTRestResponse = GroupKTHelper.getRestResponse(groupKTResponse);
        GroupKTState groupKTState = GroupKTHelper.getResult(groupKTRestResponse);
        State state = stateTransformer.transform(groupKTState);
        return ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

}
