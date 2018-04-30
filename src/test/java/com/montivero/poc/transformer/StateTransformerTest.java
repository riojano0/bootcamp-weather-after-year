package com.montivero.poc.transformer;

import com.montivero.poc.client.domain.groupKT.GroupKTState;
import com.montivero.poc.resource.domain.State;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class StateTransformerTest {

    private GroupKTState groupKTState;
    private StateTransformer stateTransformer;

    @Before
    public void setUp() {
        stateTransformer = new StateTransformer();
        groupKTState = new GroupKTState();
        groupKTState.setName("Central Dominance");
        groupKTState.setCapital("Luthadel");
        groupKTState.setArea("MultipleKms");
        groupKTState.setAbbreviateName("CD");
        groupKTState.setCountry("SCA");
        groupKTState.setLargestCity("Luthadel");
    }

    @Test
    public void shouldTransformGroupKTStateToNewStateWhenIsNull() {

        State state = stateTransformer.transform(null);

        assertThat(state.getName(), nullValue());
        assertThat(state.getCapital(), nullValue());
        assertThat(state.getArea(), nullValue());
        assertThat(state.getShortName(), nullValue());
        assertThat(state.getCountryShortName3(), nullValue());
        assertThat(state.getLargestCity(), nullValue());
    }

    @Test
    public void shouldTransformGroupKTStateToStateWhenIsNotNull() {

        State state = stateTransformer.transform(groupKTState);

        assertThat(state.getName(), is("Central Dominance"));
        assertThat(state.getCapital(), is("Luthadel"));
        assertThat(state.getArea(), is("MultipleKms"));
        assertThat(state.getShortName(), is("CD"));
        assertThat(state.getCountryShortName3(), is("SCA"));
        assertThat(state.getLargestCity(), is("Luthadel"));
    }

    @Test
    public void shouldTransformGroupKTStateListToStateListWhenIsNotEmpty() {

        List<State> stateList = stateTransformer.transformList(Collections.singletonList(groupKTState));

        assertThat(stateList, hasSize(1));
        assertThat(stateList.get(0).getName(), is("Central Dominance"));
        assertThat(stateList.get(0).getCapital(), is("Luthadel"));
        assertThat(stateList.get(0).getArea(), is("MultipleKms"));
        assertThat(stateList.get(0).getShortName(), is("CD"));
        assertThat(stateList.get(0).getCountryShortName3(), is("SCA"));
        assertThat(stateList.get(0).getLargestCity(), is("Luthadel"));
    }

    @Test
    public void shouldTransformGroupKTCountryListToCountryListEmptyWhenIsNull() {

        List<State> stateList = stateTransformer.transformList(null);

        assertThat(stateList, hasSize(0));
    }

    @Test
    public void shouldTransformGroupKTCountryListToCountryListEmptyWhenIsEmpty() {

        List<State> stateList = stateTransformer.transformList(new ArrayList<>());

        assertThat(stateList, hasSize(0));
    }

}