package de.chkal.backset.server;

import org.slf4j.bridge.SLF4JBridgeHandler;

import de.chkal.backset.core.Backset;
import de.chkal.backset.core.ServiceLoaderModuleProvider;
import de.chkal.backset.core.config.DefaultConfigManager;
import de.chkal.backset.core.config.DefaultConfigManagerBuilder;

public class Bootstrap {

  public static void main(String[] args) {

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    DefaultConfigManager configManager = new DefaultConfigManagerBuilder()
        .addClasspathConfig("backset.yml")
        .build();

    Backset backset = Backset.builder()
        .configManager(configManager)
        .moduleProvider(new ServiceLoaderModuleProvider())
        .build();
    backset.start();

  }
}
