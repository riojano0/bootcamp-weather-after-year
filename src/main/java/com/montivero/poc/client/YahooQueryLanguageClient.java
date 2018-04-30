package com.montivero.poc.client;

import com.montivero.poc.client.domain.yahooQuery.YahooQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

public interface YahooQueryLanguageClient {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    YahooQuery getQuery(@QueryParam("q") String query, @DefaultValue("json") @QueryParam("format") String format);

}
