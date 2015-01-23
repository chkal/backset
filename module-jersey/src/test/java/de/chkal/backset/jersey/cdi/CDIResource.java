package de.chkal.backset.jersey.cdi;

import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/cdi")
public class CDIResource {

  // @Inject
  private CIDBean injected;

  @GET
  @Path("/lookup")
  public Response getLookup() {
    CIDBean lookup = CDI.current().select(CIDBean.class).get();
    return buildResponse(lookup);
  }

  @GET
  @Path("/inject")
  public Response getInject() {
    return buildResponse(injected);
  }

  private Response buildResponse(CIDBean bean) {
    String text = "not found";
    if (bean != null) {
      text = bean.getText();
    }
    return Response.ok(text).build();
  }

}
