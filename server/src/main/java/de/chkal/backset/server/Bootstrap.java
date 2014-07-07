package de.chkal.backset.server;

import org.slf4j.bridge.SLF4JBridgeHandler;

import de.chkal.backset.core.Backset;
import de.chkal.backset.core.ServiceLoaderModuleProvider;
import de.chkal.backset.core.config.DefaultConfigManager;
import de.chkal.backset.core.config.DefaultConfigManagerBuilder;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.server.config.LoggingConfig;

public class Bootstrap {

  public static void main(String[] args) {

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    DefaultConfigManager configManager = new DefaultConfigManagerBuilder()
        .addClasspathConfig("backset.yml")
        .build();

    configureLogging(configManager);

    Backset backset = Backset.builder()
        .configManager(configManager)
        .moduleProvider(new ServiceLoaderModuleProvider())
        .build();
    backset.start();

  }

  private static void configureLogging(ConfigManager configManager) {

    LoggingConfig loggingConfig = configManager.getConfig(LoggingConfig.NAME, LoggingConfig.class);

    LoggingConfigurator configurator = new LoggingConfigurator();
    if (loggingConfig != null) {
      configurator.setConfig(loggingConfig);
    }

    configurator.configure();

  }
}
