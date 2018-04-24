package com.montivero.poc.delegate;

import com.montivero.poc.client.StateNameClient;
import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTRestResponse;
import com.montivero.poc.client.domain.GroupKTState;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.resource.domain.State;
import com.montivero.poc.transformer.StateTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StateDelegate.class, GroupKTHelper.class, ResponseEntityHelper.class})
public class StateDelegateTest {

    private static final String COUNTRY_CODE = "SCA";
    private static final String ABBREVIATE_NAME = "LU";
    private static final String CAPITAL_NAME = "Luthadel";
    private static final String STATE_NAME = "Central Dominance";
    private State state;
    private List<State> stateList;
    private GroupKTState groupKTState;
    private List<GroupKTState> groupKTStateList;
    private ResponseEntity responseEntitySuccessful;

    @Mock
    private StateNameClient mockStateNameClient;
    @Mock
    private StateTransformer mockStateTransformer;

    private StateDelegate stateDelegate;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(GroupKTHelper.class);
        mockStatic(ResponseEntityHelper.class);
        stateDelegate = new StateDelegate(mockStateNameClient, mockStateTransformer);

        state = State.builder().capital(CAPITAL_NAME).name(STATE_NAME).build();
        stateList = Collections.singletonList(state);

        groupKTState = new GroupKTState();
        groupKTState.setCapital(CAPITAL_NAME);
        groupKTState.setName(STATE_NAME);
        groupKTState.setAbbreviateName(ABBREVIATE_NAME);
        groupKTStateList = new ArrayList<>();
        groupKTStateList.add(groupKTState);
        responseEntitySuccessful = new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    @Test
    public void shouldGetAllStatesByCountry() {
        GroupKTRestResponse<List<GroupKTState>> groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTStateList);
        GroupKTResponse<List<GroupKTState>> groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(mockStateNameClient.getAllStateByCountry(COUNTRY_CODE)).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTStateList);
        when(mockStateTransformer.transformList(groupKTStateList)).thenReturn(stateList);
        when(ResponseEntityHelper.prepareResponseEntityForList(stateList)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getAllStatesByCountry(COUNTRY_CODE);

        assertThat(responseEntity, notNullValue());
        verify(mockStateNameClient).getAllStateByCountry(COUNTRY_CODE);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockStateTransformer).transformList(groupKTStateList);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForList(stateList);
    }

    @Test
    public void getStateByCountryAndShortName() {
        GroupKTRestResponse<GroupKTState> groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTState);
        GroupKTResponse<GroupKTState> groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(mockStateNameClient.getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME)).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTState);
        when(mockStateTransformer.transform(groupKTState)).thenReturn(state);
        when(ResponseEntityHelper.prepareResponseEntityForLocation(state)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getStateByCountryAndShortName(COUNTRY_CODE, ABBREVIATE_NAME);

        assertThat(responseEntity, notNullValue());
        verify(mockStateNameClient).getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockStateTransformer).transform(groupKTState);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

    @Test
    public void shouldCallClientWithUpperCase() {

        stateDelegate.getAllStatesByCountry(COUNTRY_CODE.toLowerCase());

        verify(mockStateNameClient).getAllStateByCountry(COUNTRY_CODE);

        stateDelegate.getStateByCountryAndShortName(COUNTRY_CODE.toLowerCase(), ABBREVIATE_NAME.toLowerCase());

        verify(mockStateNameClient).getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME);
    }
}