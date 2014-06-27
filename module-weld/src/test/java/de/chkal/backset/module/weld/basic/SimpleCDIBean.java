package de.chkal.backset.module.weld.basic;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleCDIBean {

  private String message;

  @PostConstruct
  public void init() {
    message = "I'm a CDI bean";
  }

  public String getMessage() {
    return message;
  }

}
