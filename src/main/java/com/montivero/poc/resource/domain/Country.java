package com.montivero.poc.resource.domain;

import com.montivero.poc.resource.domain.persistence.LongIdentifiable;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "COUNTRIES")
public class Country extends LongIdentifiable implements Location {

    @Column(name = "NAME")
    private String name;
    @Column(name = "SHORT_NAME_2")
    private String shortName2;
    @Column(name = "SHORT_NAME_3")
    private String shortName3;

}
