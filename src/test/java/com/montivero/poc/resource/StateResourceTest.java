package com.montivero.poc.resource;

import com.montivero.poc.delegate.StateDelegate;
import com.montivero.poc.resource.domain.State;
import com.montivero.poc.resource.domain.exception.ValidationException;
import com.montivero.poc.resource.validator.StateValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@Ignore
public class StateResourceTest {

    private static final String COUNTRY_CODE = "SCA";
    private static final String SHORT_STATE_NAME = "LU";
    private ResponseEntity responseEntitySuccessful;

    @Mock
    private StateDelegate mockStateDelegate;

    @Mock
    private StateValidator stateValidator;

    private StateResource stateResource;

    @Before
    public void setUp() {
        initMocks(this);
        stateResource = new StateResource(mockStateDelegate, stateValidator);

        responseEntitySuccessful = new ResponseEntity<>("Successful", HttpStatus.OK);
    }

    @Test
    public void shouldGetAllStateByCountry() {
        when(mockStateDelegate.getAllStatesByCountry(COUNTRY_CODE)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateResource.getAllStatesByCountry(COUNTRY_CODE);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateDelegate).getAllStatesByCountry(COUNTRY_CODE);
    }

    @Test
    public void shouldGetStateByCountryAndShortStateName() {
        when(mockStateDelegate.getStateByCountryAndShortName(COUNTRY_CODE, SHORT_STATE_NAME)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateResource.getStateByCountryAndShortName(COUNTRY_CODE, SHORT_STATE_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockStateDelegate).getStateByCountryAndShortName(COUNTRY_CODE, SHORT_STATE_NAME);
    }

    @Test
    public void shouldSaveState() {
        State state = new State();
        when(mockStateDelegate.saveState(state)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = stateResource.saveState(state);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(stateValidator).validate(state);
        verify(mockStateDelegate).saveState(state);
    }

    @Test(expected = ValidationException.class)
    public void shouldThrowValidationExceptionWhenStateToSaveIsInvalid() {
        final String validationExceptionKey = "state.countryShortName3";
        final String validationExceptionValue = "FourLetters";
        final String validationExceptionMessage = "state.countryShortName3";
        State state = State.builder().countryShortName3(validationExceptionValue).build();
        doThrow(new ValidationException(validationExceptionKey, validationExceptionValue, validationExceptionMessage))
                .when(stateValidator).validate(state);

        try {
            stateResource.saveState(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(validationExceptionKey));
            assertThat(e.getValue(), is(validationExceptionValue));
            assertThat(e.getMessage(), is(validationExceptionMessage));
            verifyZeroInteractions(mockStateDelegate);

            throw e;
        }
    }
}