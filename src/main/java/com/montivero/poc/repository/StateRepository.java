package com.montivero.poc.repository;

import com.montivero.poc.resource.domain.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State, Long> {

    State findByCountryShortName3AndShortName(String countryShortName3, String shortName);

    List<State> findAllByCountryShortName3(String countryShortName3);

}
