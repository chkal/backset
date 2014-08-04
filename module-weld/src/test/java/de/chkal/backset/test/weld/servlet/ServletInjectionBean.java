package de.chkal.backset.test.weld.servlet;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServletInjectionBean {

  public String getName() {
    return "Some CDI bean";
  }

}
