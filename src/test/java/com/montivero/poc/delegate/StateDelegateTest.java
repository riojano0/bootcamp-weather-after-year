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

    private State state;
    private List<State> stateList;
    private GroupKTState groupKTState;
    private List<GroupKTState> groupKTStateList;

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

        state = State.builder().name("Luthadel").name("Central Dominance").build();
        stateList = Collections.singletonList(state);

        groupKTState = new GroupKTState();
        groupKTState.setCapital("Luthadel");
        groupKTState.setName("Central Dominance");
        groupKTState.setAbbreviateName("LU");
        groupKTStateList = new ArrayList<>();
        groupKTStateList.add(groupKTState);
    }

    @Test
    public void shouldGetAllStatesByCountry() {
        GroupKTRestResponse<List<GroupKTState>> groupKTRestResponseList = new GroupKTRestResponse<>();
        groupKTRestResponseList.setResult(groupKTStateList);
        GroupKTResponse<List<GroupKTState>> groupKTResponseList = new GroupKTResponse<>();
        groupKTResponseList.setRestResponse(groupKTRestResponseList);
        ResponseEntity<List<State>> responseEntityOk = new ResponseEntity<>(stateList, HttpStatus.OK);
        when(mockStateNameClient.getAllStateByCountry("SCA")).thenReturn(groupKTResponseList);
        when(GroupKTHelper.getRestResponse(groupKTResponseList)).thenReturn(groupKTRestResponseList);
        when(GroupKTHelper.getResult(groupKTRestResponseList)).thenReturn(groupKTStateList);
        when(mockStateTransformer.transformList(groupKTStateList)).thenReturn(stateList);
        when(ResponseEntityHelper.prepareResponseEntityForList(stateList)).thenReturn(responseEntityOk);

        ResponseEntity responseEntity = stateDelegate.getAllStatesByCountry("SCA");

        assertThat(responseEntity, notNullValue());
        verify(mockStateNameClient).getAllStateByCountry("SCA");
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponseList);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponseList);
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
        ResponseEntity<State> responseEntityOk = new ResponseEntity<>(state, HttpStatus.OK);
        when(mockStateNameClient.getStateDetailsByCountryAndAbbrName("SCA", "LU")).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTState);
        when(mockStateTransformer.transform(groupKTState)).thenReturn(state);
        when(ResponseEntityHelper.prepareResponseEntityForLocation(state)).thenReturn(responseEntityOk);

        ResponseEntity responseEntity = stateDelegate.getStateByCountryAndShortName("SCA", "LU");

        assertThat(responseEntity, notNullValue());
        verify(mockStateNameClient).getStateDetailsByCountryAndAbbrName("SCA", "LU");
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockStateTransformer).transform(groupKTState);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

}