package de.chkal.backset.test.weld.jsf.simple;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class SimpleWeldFacesBean {

  private String message;

  @PostConstruct
  public void init() {
    message = "I'm a CDI bean";
  }

  public String getMessage() {
    return message;
  }

}
