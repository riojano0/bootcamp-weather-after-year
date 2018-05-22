package com.montivero.poc.resource.validator;

import com.montivero.poc.resource.domain.State;
import com.montivero.poc.resource.domain.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class StateValidatorTest {

    private static final String STATE = "state";
    private static final String STATE_NAME = "state.name";
    private static final String STATE_SHORT_NAME = "state.shortName";
    private static final String STATE_AREA = "state.area";
    private static final String STATE_LARGEST_CITY = "state.largestCity";
    private static final String STATE_CAPITAL = "state.capital";
    private static final String STATE_COUNTRY_SHORT_NAME3 = "state.countryShortName3";
    private static final String STRING_WITH_LENGTH_101 = StringUtils.repeat("a", 101);

    private StateValidator stateValidator;
    private State state;

    @Before
    public void setUp() {
        stateValidator = new StateValidator();
        state = State.builder()
                .shortName("LU")
                .name("Luthadel")
                .area("100km")
                .capital("Central Dominance")
                .largestCity("Central Dominance")
                .countryShortName3("SCA")
                .build();
    }

    @Test
    public void shouldValidateValidState() {
        stateValidator.validate(state);
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenStateIsNull() {
        try {
            stateValidator.validate(null);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE));
            assertThat(e.getValue(), nullValue());
            assertThat(e.getMessage(), is("state is null"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenNameIsNull() {
        state.setName(null);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_NAME));
            assertThat(e.getValue(), nullValue());
            assertThat(e.getMessage(), is("state.name is null"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenNameIsLowerThatMinLength() {
        state.setName("a");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_NAME));
            assertThat(e.getValue(), is("a"));
            assertThat(e.getMessage(), is("state.name need to have length between 2 and 100"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenNameIsHigherThatMaxLength() {
        state.setName(STRING_WITH_LENGTH_101);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_NAME));
            assertThat(e.getValue(), is(STRING_WITH_LENGTH_101));
            assertThat(e.getMessage(), is("state.name need to have length between 2 and 100"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenShortNameIsNull() {
        state.setShortName(null);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_SHORT_NAME));
            assertThat(e.getValue(), nullValue());
            assertThat(e.getMessage(), is("state.shortName is null"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenShortNameIsLowerThatMinLength() {
        state.setShortName("a");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_SHORT_NAME));
            assertThat(e.getValue(), is("a".toUpperCase()));
            assertThat(e.getMessage(), is("state.shortName need to have length between 2 and 3"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenShortNameIsHigherThatMaxLength() {
        state.setShortName(STRING_WITH_LENGTH_101);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_SHORT_NAME));
            assertThat(e.getValue(), is(STRING_WITH_LENGTH_101.toUpperCase()));
            assertThat(e.getMessage(), is("state.shortName need to have length between 2 and 3"));

            throw e;
        }
    }

    @Test
    public void shouldValidateAndAllowNullArea() {
        state.setArea(null);

        stateValidator.validate(state);
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenAreaIsHigherThatMaxLength() {
        state.setArea(STRING_WITH_LENGTH_101);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_AREA));
            assertThat(e.getValue(), is(STRING_WITH_LENGTH_101));
            assertThat(e.getMessage(), is("state.area max length is 100 characters"));

            throw e;
        }
    }

    @Test
    public void shouldValidateAndAllowNullLargestCity() {
        state.setLargestCity(null);

        stateValidator.validate(state);

    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenLargestCityIsLowerThatMinLength() {
        state.setLargestCity("a");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_LARGEST_CITY));
            assertThat(e.getValue(), is("a"));
            assertThat(e.getMessage(), is("state.largestCity need to have length between 2 and 100"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenLargestCityIsHigherThatMaxLength() {
        state.setLargestCity(STRING_WITH_LENGTH_101);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_LARGEST_CITY));
            assertThat(e.getValue(), is(STRING_WITH_LENGTH_101));
            assertThat(e.getMessage(), is("state.largestCity need to have length between 2 and 100"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenCapitalIsNull() {
        state.setCapital(null);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_CAPITAL));
            assertThat(e.getValue(), nullValue());
            assertThat(e.getMessage(), is("state.capital is null"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenCapitalIsLowerThatMinLength() {
        state.setCapital("a");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_CAPITAL));
            assertThat(e.getValue(), is("a"));
            assertThat(e.getMessage(), is("state.capital need to have length between 2 and 100"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenCapitalIsHigherThatMaxLength() {
        state.setCapital(STRING_WITH_LENGTH_101);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_CAPITAL));
            assertThat(e.getValue(), is(STRING_WITH_LENGTH_101));
            assertThat(e.getMessage(), is("state.capital need to have length between 2 and 100"));

            throw e;
        }
    }

    //

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenCountryShortName3IsNull() {
        state.setCountryShortName3(null);

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_COUNTRY_SHORT_NAME3));
            assertThat(e.getValue(), nullValue());
            assertThat(e.getMessage(), is("state.countryShortName3 is null"));

            throw e;
        }
    }

    @Test(expected = ValidationException.class)
    public void shouldValidateThrowValidateExceptionWhenCountryShortName3LengthIsNotTheSpecificLength() {
        state.setCountryShortName3("a");

        try {
            stateValidator.validate(state);
        } catch (ValidationException e) {
            assertThat(e.getKey(), is(STATE_COUNTRY_SHORT_NAME3));
            assertThat(e.getValue(), is("a".toUpperCase()));
            assertThat(e.getMessage(), is("state.countryShortName3 need to have the length of 3"));

            throw e;
        }
    }

}