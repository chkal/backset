package de.chkal.backset.test.weld.multibda;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MultipleBDAsBean {

  @First
  @Second
  public String getResult() {
    return "raw-result";
  }

}
