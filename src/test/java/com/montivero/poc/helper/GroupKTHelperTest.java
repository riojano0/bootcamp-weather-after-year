package com.montivero.poc.helper;

import com.montivero.poc.client.domain.groupKT.GroupKTResponse;
import com.montivero.poc.client.domain.groupKT.GroupKTRestResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GroupKTHelperTest {

    private GroupKTResponse<String> groupKTResponse;
    private GroupKTRestResponse<String> groupKTRestResponse;

    @Before
    public void setUp() {
        groupKTResponse = new GroupKTResponse<>();
        groupKTRestResponse = new GroupKTRestResponse<>();
    }

    @Test
    public void shouldGetNullFromGroupKTResponseWhenIsNull() {
        assertThat(GroupKTHelper.getRestResponse(null), nullValue());
    }

    @Test
    public void shouldGetNullFromGroupKTResponseWhenIsNullTheContent() {
        groupKTResponse.setRestResponse(null);

        assertThat(GroupKTHelper.getRestResponse(groupKTResponse), nullValue());
    }

    @Test
    public void shouldGetGroupKTRestResponseFromGroupKTResponseWhenIsNotNullTheContent() {
        groupKTRestResponse.setResult("result values");
        groupKTResponse.setRestResponse(groupKTRestResponse);

        GroupKTRestResponse groupKTRestResponse = GroupKTHelper.getRestResponse(groupKTResponse);

        assertThat(groupKTRestResponse, notNullValue());
        assertThat(groupKTRestResponse.getResult(), is("result values"));
    }

    @Test
    public void shouldGetNullFromGroupKTRestResponseWhenIsNull() {
        assertThat(GroupKTHelper.getResult(null), nullValue());
    }

    @Test
    public void shouldGetNullFromGroupKTRestResponseWhenIsNullTheResult() {
        groupKTResponse.setRestResponse(null);

        assertThat(GroupKTHelper.getResult(groupKTRestResponse), nullValue());
    }

    @Test
    public void shouldGetResultFromGroupKTRestResponseWhenIsNotNullTheResult() {
        groupKTRestResponse.setResult("result values");

        String result = GroupKTHelper.getResult(groupKTRestResponse);

        assertThat(result, notNullValue());
        assertThat(result, is("result values"));
    }

}