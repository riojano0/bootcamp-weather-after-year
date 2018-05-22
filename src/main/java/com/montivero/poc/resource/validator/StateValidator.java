package com.montivero.poc.resource.validator;

import com.montivero.poc.resource.domain.State;
import com.montivero.poc.resource.domain.exception.ValidationException;
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
        final int minLength = 2;
        final int maxLength = 100;
        validateNotNull(name, field);
        if (name.length() < minLength || name.length() > maxLength) {
            throw new ValidationException(field, name, lengthRangeMessage(field, minLength, maxLength));
        }
    }

    private void validateShortName(String shortName) {
        final String field = "state.shortName";
        validateNotNull(shortName, field);
        final int minLength = 2;
        final int maxLength = 3;
        if (shortName.length() < minLength || shortName.length() > maxLength) {
            throw new ValidationException(field, shortName, lengthRangeMessage(field, minLength, maxLength));
        }
    }

    private void validateArea(String area) {
        final String field = "state.area";
        final int maxLength = 100;
        if (StringUtils.isNotBlank(area) && area.length() > maxLength) {
            throw new ValidationException(field, area, lengthMaxMessage(field, maxLength));
        }
    }

    private void validateLargestCity(String largestCity) {
        final String field = "state.largestCity";
        final int minLength = 2;
        final int maxLength = 100;
        if (StringUtils.isNotBlank(largestCity) && (largestCity.length() < minLength || largestCity.length() > maxLength)) {
            throw new ValidationException(field, largestCity, lengthRangeMessage(field, minLength, maxLength));
        }
    }

    private void validateCapital(String capital) {
        final String field = "state.capital";
        validateNotNull(capital, field);
        final int minLength = 2;
        final int maxLength = 100;
        if (capital.length() < minLength || capital.length() > maxLength) {
            throw new ValidationException(field, capital, lengthRangeMessage(field, minLength, maxLength));
        }
    }

    private void validateCountryShortName3(String countryShortName3) {
        final String field = "state.countryShortName3";
        validateNotNull(countryShortName3, field);
        final int specificLength = 3;
        if (countryShortName3.trim().length() != specificLength) {
            throw new ValidationException(field, countryShortName3, lengthSpecificMessage(field, specificLength));
        }
    }

}
