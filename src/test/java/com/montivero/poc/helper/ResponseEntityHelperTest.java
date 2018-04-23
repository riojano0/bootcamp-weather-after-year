package com.montivero.poc.helper;

import com.montivero.poc.resource.domain.Country;
import com.montivero.poc.resource.domain.Location;
import com.montivero.poc.resource.domain.Message;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ResponseEntityHelperTest {

    private static final String DEFAULT_MESSAGE_NOT_FOUND_MATCH = "Not found Match";
    private static final String COUNTRY_NAME = "Argentina";
    private ResponseEntity<?> responseEntity;
    private Location location;

    @Test
    public void shouldGetResponseEntityWithStatusCode200WithContentWhenLocationNameIsNotNull() {
        location = Country.builder().name(COUNTRY_NAME).build();

        responseEntity = ResponseEntityHelper.prepareResponseEntityForLocation(location);

        assertThat(responseEntity.getStatusCodeValue(), is(HttpStatus.OK.value()));
        assertThat(responseEntity.getBody(), is(location));
    }

    @Test
    public void shouldGetResponseEntityWithStatusCode404WithMessageWhenLocationNameIsNull() {
        location = Country.builder().name(null).build();

        responseEntity = ResponseEntityHelper.prepareResponseEntityForLocation(location);

        assertThat(responseEntity.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));
        Message message = (Message) responseEntity.getBody();
        assertThat(message.getValue(), is(DEFAULT_MESSAGE_NOT_FOUND_MATCH));
    }

    @Test
    public void shouldGetResponseEntityWithStatusCode200WithContentWhenListIsNotEmpty() {
        location = Country.builder().name(COUNTRY_NAME).build();
        List<Location> locationList = Collections.singletonList(location);

        responseEntity = ResponseEntityHelper.prepareResponseEntityForList(locationList);

        assertThat(responseEntity.getStatusCodeValue(), is(HttpStatus.OK.value()));

        List<?> locations = (List<?>) responseEntity.getBody();
        assertThat(locations, hasSize(1));
        Location locationFromResponseEntity = (Location) locations.get(0);
        assertThat(locationFromResponseEntity.getName(), is(COUNTRY_NAME));
    }

    @Test
    public void shouldGetResponseEntityWithStatusCode404WithMessageWhenListIsNull() {

        responseEntity = ResponseEntityHelper.prepareResponseEntityForList(null);

        assertThat(responseEntity.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));
        Message message = (Message) responseEntity.getBody();
        assertThat(message.getValue(), is(DEFAULT_MESSAGE_NOT_FOUND_MATCH));
    }

    @Test
    public void shouldGetResponseEntityWithStatusCode404WithMessageWhenListIsEmpty() {

        responseEntity = ResponseEntityHelper.prepareResponseEntityForList(new ArrayList<>());

        assertThat(responseEntity.getStatusCodeValue(), is(HttpStatus.NOT_FOUND.value()));
        Message message = (Message) responseEntity.getBody();
        assertThat(message.getValue(), is(DEFAULT_MESSAGE_NOT_FOUND_MATCH));
    }

}