package com.montivero.poc.client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.ws.Response;

public interface YahooQueryLanguageClient {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    Response getQuery(@QueryParam("q") String query, @DefaultValue("json") @QueryParam("format") String format);

}
