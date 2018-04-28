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
import static org.hamcrest.Matchers.is;
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
    private static final String MESSAGE_VALUE_STATE_SAVED = "State Saved";
    private static final String MESSAGE_VALUE_STATE_ALREADY_SAVE = "State Already Save";

    private State state;
    private List<State> stateList;
    private GroupKTState groupKTState;
    private List<GroupKTState> groupKTStateList;
    private ResponseEntity responseEntitySuccessful;

    @Mock
    private StateNameClient mockStateNameClient;
    @Mock
    private StateTransformer mockStateTransformer;
    @Mock
    private StateRepository mockStateRepository;

    private StateDelegate stateDelegate;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(GroupKTHelper.class);
        mockStatic(ResponseEntityHelper.class);
        stateDelegate = new StateDelegate(mockStateNameClient, mockStateTransformer, mockStateRepository);

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
    public void shouldGetAllStatesByCountryFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResults() {
        GroupKTRestResponse<List<GroupKTState>> groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTStateList);
        GroupKTResponse<List<GroupKTState>> groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(mockStateRepository.findAllByCountryShortName3(COUNTRY_CODE)).thenReturn(null);
        when(mockStateNameClient.getAllStateByCountry(COUNTRY_CODE)).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTStateList);
        when(mockStateTransformer.transformList(groupKTStateList)).thenReturn(stateList);
        when(ResponseEntityHelper.prepareResponseEntityForList(stateList)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getAllStatesByCountry(COUNTRY_CODE);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findAllByCountryShortName3(COUNTRY_CODE);
        verify(mockStateNameClient).getAllStateByCountry(COUNTRY_CODE);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockStateTransformer).transformList(groupKTStateList);
        verify(mockStateRepository).saveAll(stateList);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForList(stateList);
    }

    @Test
    public void shouldGetAllStatesByCountryFromDatabaseWhenHaveMatch() {
        when(mockStateRepository.findAllByCountryShortName3(COUNTRY_CODE)).thenReturn(stateList);
        when(ResponseEntityHelper.prepareResponseEntityForList(stateList)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getAllStatesByCountry(COUNTRY_CODE);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findAllByCountryShortName3(COUNTRY_CODE);
        verify(mockStateNameClient, never()).getAllStateByCountry(COUNTRY_CODE);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(any());
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(any());
        verify(mockStateTransformer, never()).transformList(groupKTStateList);
        verify(mockStateRepository, never()).saveAll(stateList);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForList(stateList);
    }

    @Test
    public void getStateByCountryAndShortNameFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResult() {
        GroupKTRestResponse<GroupKTState> groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTState);
        GroupKTResponse<GroupKTState> groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(mockStateRepository.findByCountryShortName3AndShortName(COUNTRY_CODE, ABBREVIATE_NAME)).thenReturn(null);
        when(mockStateNameClient.getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME)).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTState);
        when(mockStateTransformer.transform(groupKTState)).thenReturn(state);
        when(ResponseEntityHelper.prepareResponseEntityForLocation(state)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getStateByCountryAndShortName(COUNTRY_CODE, ABBREVIATE_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findByCountryShortName3AndShortName(COUNTRY_CODE, ABBREVIATE_NAME);
        verify(mockStateNameClient).getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockStateTransformer).transform(groupKTState);
        verify(mockStateRepository).save(state);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

    @Test
    public void getStateByCountryAndShortNameFromDatabaseWhenHaveMatch() {
        when(mockStateRepository.findByCountryShortName3AndShortName(COUNTRY_CODE, ABBREVIATE_NAME)).thenReturn(state);
        when(ResponseEntityHelper.prepareResponseEntityForLocation(state)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.getStateByCountryAndShortName(COUNTRY_CODE, ABBREVIATE_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findByCountryShortName3AndShortName(COUNTRY_CODE, ABBREVIATE_NAME);
        verify(mockStateNameClient, never()).getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(any());
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(any());
        verify(mockStateTransformer, never()).transform(groupKTState);
        verify(mockStateRepository, never()).save(state);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(state);
    }

    @Test
    public void shouldSaveNewStateWhenNotExistMatchOnDatabase() {
        when(mockStateRepository.findByCountryShortName3AndShortName(state.getCountryShortName3(), state.getShortName())).thenReturn(null);
        when(ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_SAVED), HttpStatus.OK)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.saveState(state);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findByCountryShortName3AndShortName(state.getCountryShortName3(), state.getShortName());
        verify(mockStateRepository).save(state);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_SAVED), HttpStatus.OK);
    }

    @Test
    public void shouldNotSaveNewStateWhenExistMatchOnDatabase() {
        when(mockStateRepository.findByCountryShortName3AndShortName(state.getCountryShortName3(), state.getShortName())).thenReturn(state);
        when(ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_ALREADY_SAVE), HttpStatus.OK)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateDelegate.saveState(state);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateRepository).findByCountryShortName3AndShortName(state.getCountryShortName3(), state.getShortName());
        verify(mockStateRepository, never()).save(state);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_ALREADY_SAVE), HttpStatus.OK);
    }

    @Test
    public void shouldCallClientWithUpperCase() {

        stateDelegate.getAllStatesByCountry(COUNTRY_CODE.toLowerCase());

        verify(mockStateNameClient).getAllStateByCountry(COUNTRY_CODE);

        stateDelegate.getStateByCountryAndShortName(COUNTRY_CODE.toLowerCase(), ABBREVIATE_NAME.toLowerCase());

        verify(mockStateNameClient).getStateDetailsByCountryAndAbbrName(COUNTRY_CODE, ABBREVIATE_NAME);
    }
}