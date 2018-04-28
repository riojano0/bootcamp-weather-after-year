package com.montivero.poc.delegate;

import com.montivero.poc.client.StateNameClient;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.client.domain.GroupKTState;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.repository.StateRepository;
import com.montivero.poc.resource.domain.Message;
import com.montivero.poc.resource.domain.State;
import com.montivero.poc.transformer.StateTransformer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StateDelegate {

    private final StateNameClient stateNameClient;
    private final StateTransformer stateTransformer;
    private final StateRepository stateRepository;

    @Autowired
    public StateDelegate(StateNameClient stateNameClient, StateTransformer stateTransformer, StateRepository stateRepository) {
        this.stateNameClient = stateNameClient;
        this.stateTransformer = stateTransformer;
        this.stateRepository = stateRepository;
    }

    public ResponseEntity getAllStatesByCountry(String countryCode) {
        List<State> states = stateRepository.findAllByCountryShortName3(countryCode);
        if (CollectionUtils.isEmpty(states)) {
            GroupKTResponse<List<GroupKTState>> groupKTResponse = stateNameClient.getAllStateByCountry(StringUtils.upperCase(countryCode));
            GroupKTRestResponse<List<GroupKTState>> groupKTRestResponse = GroupKTHelper.getRestResponse(groupKTResponse);
            List<GroupKTState> groupKTStates = GroupKTHelper.getResult(groupKTRestResponse);
            states = stateTransformer.transformList(groupKTStates);
            stateRepository.saveAll(states);
        }
        return ResponseEntityHelper.prepareResponseEntityForList(states);
    }

    public ResponseEntity getStateByCountryAndShortName(String countryCode, String shortStateName) {
        State state = stateRepository.findByCountryShortName3AndShortName(countryCode, shortStateName);
        if (state == null) {
            GroupKTResponse<GroupKTState> groupKTResponse = stateNameClient.getStateDetailsByCountryAndAbbrName(StringUtils.upperCase(countryCode), StringUtils.upperCase(shortStateName));
            GroupKTRestResponse<GroupKTState> groupKTRestResponse = GroupKTHelper.getRestResponse(groupKTResponse);
            GroupKTState groupKTState = GroupKTHelper.getResult(groupKTRestResponse);
            state = stateTransformer.transform(groupKTState);
            stateRepository.save(state);
        }
        return ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

    public ResponseEntity saveState(State state) {
        State stateEntity = stateRepository.findByCountryShortName3AndShortName(state.getCountryShortName3(), state.getShortName());
        StringBuilder messageValueStringBuilder = new StringBuilder("State Already Save");
        if (stateEntity == null) {
            stateRepository.save(state);
            messageValueStringBuilder = new StringBuilder("State Saved");
        }
        return ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(messageValueStringBuilder.toString()), HttpStatus.OK);
    }

}
