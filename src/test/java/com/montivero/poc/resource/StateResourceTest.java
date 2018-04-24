package com.montivero.poc.resource;

import com.montivero.poc.delegate.StateDelegate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class StateResourceTest {

    private static final String COUNTRY_CODE = "SCA";
    private static final String SHORT_STATE_NAME = "LU";
    private ResponseEntity responseEntitySuccessful;

    @Mock
    private StateDelegate mockStateDelegate;

    private StateResource stateResource;

    @Before
    public void setUp() {
        initMocks(this);
        stateResource = new StateResource(mockStateDelegate);

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

}