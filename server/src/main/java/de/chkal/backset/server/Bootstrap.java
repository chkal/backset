package de.chkal.backset.server;

import java.io.File;

import org.slf4j.bridge.SLF4JBridgeHandler;

import de.chkal.backset.core.Backset;
import de.chkal.backset.core.ServiceLoaderModuleProvider;
import de.chkal.backset.core.Singletons;
import de.chkal.backset.core.config.DefaultConfigManagerBuilder;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.server.config.LoggingConfig;

public class Bootstrap {

  public static void main(String[] args) {

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    ConfigManager configManager = buildConfigManager(args);
    Singletons.register(ConfigManager.class, configManager);

    configureLogging(configManager);

    Backset backset = Backset.builder()
        .configManager(configManager)
        .moduleProvider(new ServiceLoaderModuleProvider())
        .build();
    backset.start();

  }

  private static ConfigManager buildConfigManager(String[] args) {

    DefaultConfigManagerBuilder configManagerBuilder = new DefaultConfigManagerBuilder();

    if (args.length > 0) {
      configManagerBuilder.addConfigFile(new File(args[0]));
    }

    configManagerBuilder.addClasspathConfig("backset.yml");

    return configManagerBuilder.build();

  }

  private static void configureLogging(ConfigManager configManager) {

    LoggingConfig loggingConfig = configManager.getConfig(LoggingConfig.class);

    LoggingConfigurator configurator = new LoggingConfigurator();
    if (loggingConfig != null) {
      configurator.setConfig(loggingConfig);
    }

    configurator.configure();

  }
}
