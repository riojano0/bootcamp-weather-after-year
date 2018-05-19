package com.montivero.poc.resource.validator;

import com.montivero.poc.resource.domain.State;
import com.montivero.poc.resource.domain.error.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class StateValidator implements Validator<State> {

    @Override
    public void validate(State state) {
        validateNotNull(state, "state");
        validateName(state.getName());
        validateShortName(state.getShortName());
        validateArea(state.getArea());
        validateLargestCity(state.getLargestCity());
        validateCapital(state.getCapital());
        validateCountryShortName3(state.getCountryShortName3());
    }

    private void validateName(String name) {
        final String field = "state.name";
        validateNotNull(name, field);
        if (name.length() < 2 || name.length() > 100) {
            throw new ValidationException(field, name, "name need to have length between 2 and 100");
        }
    }

    private void validateShortName(String shortName) {
        final String field = "state.shortName";
        validateNotNull(shortName, field);
        if (shortName.length() < 2 || shortName.length() > 3) {
            throw new ValidationException(field, shortName, "shortName need to have length between 2 and 3");
        }
    }

    private void validateArea(String area) {
        final String field = "state.area";
        if (StringUtils.isNotBlank(area) && area.length() > 100) {
            throw new ValidationException(field, area, "area max length is 100 characters");
        }
    }

    private void validateLargestCity(String largestCity) {
        final String field = "state.largestCity";
        if (StringUtils.isNotBlank(largestCity) && (largestCity.length() < 2 || largestCity.length() > 100)) {
            throw new ValidationException(field, largestCity, "largestCity need to have length between 2 and 100");
        }
    }

    private void validateCapital(String capital) {
        final String field = "state.capital";
        validateNotNull(capital, field);
        if (capital.length() < 2 || capital.length() > 100) {
            throw new ValidationException(field, capital, "capital need to have length between 2 and 100");
        }
    }

    private void validateCountryShortName3(String countryShortName3) {
        final String field = "state.countryShortName3";
        validateNotNull(countryShortName3, field);
        if (countryShortName3.length() != 3) {
            throw new ValidationException(field, countryShortName3, "countryShortName3 need to have the length of 3");
        }
    }

}
