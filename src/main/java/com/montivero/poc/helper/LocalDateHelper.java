package com.montivero.poc.helper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateHelper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("EEE, dd MMM yyyy").withZone(DateTimeZone.getDefault())
            .withLocale(Locale.US);

    public static LocalDate getLocalDateFromConditionDate(String conditionDate) {
        LocalDate localDate = LocalDate.now();
        if (StringUtils.isNotBlank(conditionDate)) {
            conditionDate = conditionDate.substring(0, 16);
            localDate = LocalDate.parse(conditionDate, DATE_TIME_FORMATTER);
        }
        return localDate;
    }

}
