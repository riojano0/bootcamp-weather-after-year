package com.montivero.poc.client;

import com.montivero.poc.client.domain.groupKT.GroupKTCountry;
import com.montivero.poc.client.domain.groupKT.GroupKTResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface CountryNameClient {

    @GET
    @Path("/get/all")
    @Produces(MediaType.APPLICATION_JSON)
    GroupKTResponse<List<GroupKTCountry>> getAllCountries();

    @GET
    @Path("/get/iso2code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    GroupKTResponse<GroupKTCountry> getCountryByIso2Code(@PathParam(value = "code") String code);

    @GET
    @Path("/get/iso3code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    GroupKTResponse<GroupKTCountry> getCountryByIso3Code(@PathParam(value = "code") String code);

}
