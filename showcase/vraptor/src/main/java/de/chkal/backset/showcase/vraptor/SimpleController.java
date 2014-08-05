package de.chkal.backset.showcase.vraptor;

import javax.inject.Inject;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;

@Controller
public class SimpleController {

  @Inject
  private Result result;

  @Get("/index")
  public void get() {

    result.include("foo", "bar");
    result.forwardTo("/test.jsp");

  }

}
