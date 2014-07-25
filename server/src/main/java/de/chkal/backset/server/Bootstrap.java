package de.chkal.backset.server;

import java.io.File;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.bridge.SLF4JBridgeHandler;

import de.chkal.backset.core.Backset;
import de.chkal.backset.core.ServiceLoaderModuleProvider;
import de.chkal.backset.core.config.DefaultConfigManagerBuilder;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.module.api.Singletons;
import de.chkal.backset.server.config.LoggingConfig;

public class Bootstrap implements Daemon {

  private Backset backset;
  private String[] args;

  public static void main(String[] args) throws Exception {

    Bootstrap bootstrap = new Bootstrap();
    bootstrap.init(new SimpleDaemonContext(args));
    bootstrap.start();

  }

  @Override
  public void init(DaemonContext context) throws DaemonInitException, Exception {
    this.args = context.getArguments();
  }

  @Override
  public void start() throws Exception {

    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    ConfigManager configManager = buildConfigManager(args);
    Singletons.register(ConfigManager.class, configManager);

    configureLogging(configManager);

    backset = Backset.builder()
        .configManager(configManager)
        .moduleProvider(new ServiceLoaderModuleProvider())
        .build();
    backset.start();

  }

  @Override
  public void stop() throws Exception {

    backset.stop();

  }

  @Override
  public void destroy() {

  }

  private ConfigManager buildConfigManager(String[] args) {

    DefaultConfigManagerBuilder configManagerBuilder = new DefaultConfigManagerBuilder();

    if (args.length > 0) {
      configManagerBuilder.addConfigFile(new File(args[0]));
    }

    configManagerBuilder.addClasspathConfig("backset.yml");

    return configManagerBuilder.build();

  }

  private void configureLogging(ConfigManager configManager) {

    LoggingConfig loggingConfig = configManager.getConfig(LoggingConfig.class);

    LoggingConfigurator configurator = new LoggingConfigurator();
    if (loggingConfig != null) {
      configurator.setConfig(loggingConfig);
    }

    configurator.configure();

  }

}
