package de.chkal.backset.test.weld.alternative;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;

@Alternative
public class SecondAlternative implements AlternativeInterface {

  private String name;

  @PostConstruct
  public void init() {
    name = "Second";
  }

  @Override
  public String getName() {
    return name;
  }

}
