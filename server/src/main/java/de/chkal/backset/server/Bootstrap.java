package de.chkal.backset.server;

import de.chkal.backset.core.Backset;

public class Bootstrap {

  public static void main(String[] args) {

    Backset backset = Backset.builder().build();
    backset.start();

  }

}
