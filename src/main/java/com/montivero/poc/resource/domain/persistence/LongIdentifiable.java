package com.montivero.poc.resource.domain.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class LongIdentifiable implements Identifiable<Long> {

    private Long id;

    @Id
    @Override
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }
}
