package com.montivero.poc.helper;

import org.joda.time.LocalDate;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalDateHelperTest {

    private LocalDate localDate;

    @Test
    public void shouldGetLocalDateNowFromConditionDateWhenNull() {
        localDate = LocalDateHelper.getLocalDateFromConditionDate(null);

        assertThat(localDate, is(LocalDate.now()));
    }

    @Test
    public void shouldGetLocalDateNowFromConditionDateWhenEmpty() {
        localDate = LocalDateHelper.getLocalDateFromConditionDate("");

        assertThat(localDate, is(LocalDate.now()));
    }

    @Test
    public void shouldGetLocalDateFromConditionDateOnAbbreviateWay() {
        String conditionDate = "Mon, 30 Apr 2018";

        localDate = LocalDateHelper.getLocalDateFromConditionDate(conditionDate);

        assertThat(localDate.getDayOfMonth(), is(30));
        assertThat(localDate.getMonthOfYear(), is(4));
        assertThat(localDate.getYear(), is(2018));
    }

    @Test
    public void shouldGetLocalDateFromConditionDateAndStripForAvoidTimeZone() {
        String conditionDate = "Mon, 30 Apr 2018 10:00 AM ART";

        localDate = LocalDateHelper.getLocalDateFromConditionDate(conditionDate);

        assertThat(localDate.getDayOfMonth(), is(30));
        assertThat(localDate.getMonthOfYear(), is(4));
        assertThat(localDate.getYear(), is(2018));
    }

}