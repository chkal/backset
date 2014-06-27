package de.chkal.backset.module.weld;

import org.jboss.weld.environment.AbstractContainer;
import org.jboss.weld.environment.ContainerContext;

public class BacksetContainer extends AbstractContainer {

  @Override
  public void initialize(ContainerContext context) {
  }

  @Override
  protected String classToCheck() {
    return "de.chkal.backset.core.Backset";
  }

}
