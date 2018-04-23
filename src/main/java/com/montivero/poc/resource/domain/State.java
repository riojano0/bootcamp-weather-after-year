package com.montivero.poc.resource.domain;

import com.montivero.poc.resource.domain.persistence.LongIdentifiable;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "STATES")
public class State extends LongIdentifiable implements Location {

    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME")
    private String shortName;
    @Column(name = "AREA")
    private String area;
    @Column(name = "LARGEST_CITY")
    private String largestCity;
    @Column(name = "CAPITAL")
    private String capital;
    @Column(name = "COUNTRY_SHORT_NAME_3")
    private String countryShortName3;

    public String getShortName() {
        return StringUtils.upperCase(shortName);
    }

    public String getCountryShortName3() {
        return StringUtils.upperCase(countryShortName3);
    }
}
