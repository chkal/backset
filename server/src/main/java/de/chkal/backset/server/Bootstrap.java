package de.chkal.backset.server;

import org.slf4j.bridge.SLF4JBridgeHandler;

import de.chkal.backset.core.Backset;

public class Bootstrap {

  public static void main(String[] args) {

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    Backset backset = Backset.builder().build();
    backset.start();

  }

}
