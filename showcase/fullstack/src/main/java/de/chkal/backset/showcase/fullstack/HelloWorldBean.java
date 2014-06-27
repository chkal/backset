package de.chkal.backset.showcase.fullstack;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class HelloWorldBean {

  public Date getNow() {
    return new Date();
  }

}
