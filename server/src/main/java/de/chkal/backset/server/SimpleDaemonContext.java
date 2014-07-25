package de.chkal.backset.server;

import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;

public class SimpleDaemonContext implements DaemonContext {

  private final String[] args;

  public SimpleDaemonContext(String[] args) {
    this.args = args;
  }
  
  @Override
  public String[] getArguments() {
    return args;
  }

  @Override
  public DaemonController getController() {
    throw new UnsupportedOperationException();
  }

}
