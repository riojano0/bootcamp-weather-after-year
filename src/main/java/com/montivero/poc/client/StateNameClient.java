package com.montivero.poc.client;

import com.montivero.poc.client.domain.GroupKTResponse;
import com.montivero.poc.client.domain.GroupKTState;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/get")
public interface StateNameClient {

    @GET
    @Path("/{countryCode}/all")
    @Produces(MediaType.APPLICATION_JSON)
    GroupKTResponse<List<GroupKTState>> getAllStateByCountry(@PathParam("countryCode") String countryCode);

    @GET
    @Path("/{countryCode}/{abbreviateName}")
    @Produces(MediaType.APPLICATION_JSON)
    GroupKTResponse<GroupKTState> getStateDetailsByCountryAndAbbrName(@PathParam("countryCode") String countryCode,
                                                                      @PathParam("abbreviateName") String abbreviateName);

}
