package de.chkal.backset.jersey.simple;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class SimpleResource {

  @GET
  public Response get(@QueryParam("name") String name) {
    return Response.ok("Hello " + name).build();
  }

}
