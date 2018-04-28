package com.montivero.poc.repository;

import com.montivero.poc.resource.domain.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Long> {

    List<Country> findAll();

}
