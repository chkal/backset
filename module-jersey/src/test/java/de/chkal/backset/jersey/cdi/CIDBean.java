package de.chkal.backset.jersey.cdi;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CIDBean {

  public String getText() {
    return "I'm a CDI bean";
  }

}
