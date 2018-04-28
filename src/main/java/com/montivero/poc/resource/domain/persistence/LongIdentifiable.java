package com.montivero.poc.resource.domain.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@MappedSuperclass
public class LongIdentifiable implements Identifiable<Long> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    @JsonIgnore
    public Long getId() {
        return id;
    }

}
