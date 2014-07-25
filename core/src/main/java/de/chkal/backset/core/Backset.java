package de.chkal.backset.core;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import java.util.Map.Entry;
import java.util.Stack;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.core.annotation.ReflectionsAnnotationDatabase;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.Module;

public class Backset {

  private final Logger log = LoggerFactory.getLogger(Backset.class);

  private final ClassLoader classLoader = Backset.class.getClassLoader();

  private Undertow server;

  private final String classpathPrefix;

  private final ModuleProvider moduleProvider;

  private final ConfigManager configManager;

  private final Stack<Module> startedModules = new Stack<>();

  public static Builder builder() {
    return new Builder();
  }

  public Backset(Builder builder) {
    this.classpathPrefix = builder.classpathPrefix;
    this.moduleProvider = builder.moduleProvider;
    this.configManager = builder.configManager;
  }

  public void start() {

    ReflectionsAnnotationDatabase annotationDatabase = new ReflectionsAnnotationDatabase();
    DefaultModuleContext moduleContext =
        new DefaultModuleContext(annotationDatabase, configManager);

    for (Module module : moduleProvider.getModules(classLoader)) {
      log.debug("Starting module: {}", module.getClass().getName());
      module.init(moduleContext);
      startedModules.push(module);
    }

    try {

      ClassPathResourceManager resourceManager =
          new ClassPathResourceManager(classLoader, classpathPrefix);

      DeploymentInfo servletBuilder = Servlets.deployment()
          .setClassLoader(classLoader)
          .setResourceManager(resourceManager)
          .setContextPath("/")
          .setDeploymentName("deployment.war");

      for (DeploymentEnricher enricher : moduleContext.getDeploymentEnrichers()) {
        enricher.enrich(servletBuilder);
      }

      UndertowConfig undertowConfig = configManager.getConfig(UndertowConfig.class);

      if (undertowConfig != null && undertowConfig.getContextParams() != null) {
        for (Entry<String, String> entry : undertowConfig.getContextParams().entrySet()) {
          servletBuilder.addInitParameter(entry.getKey(), entry.getValue());
        }
      }

      DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
      manager.deploy();
      HttpHandler servletHandler = manager.start();

      Undertow.Builder undertowBuilder = Undertow.builder();
      undertowBuilder.setHandler(servletHandler);
      configure(undertowBuilder, undertowConfig);
      server = undertowBuilder.build();

      server.start();
      log.info("Backset started!");

    } catch (ServletException e) {
      throw new IllegalStateException(e);
    }

  }

  private void configure(Undertow.Builder builder, UndertowConfig config) {

    if (config != null && config.getIoThreads() > 0) {
      builder.setIoThreads(config.getIoThreads());
    }

    if (config != null && config.getWorkerThreads() > 0) {
      builder.setWorkerThreads(config.getWorkerThreads());
    }

    if (config != null && !config.getConnectors().isEmpty()) {
      for (UndertowConnectorConfig connector : config.getConnectors()) {
        addConnector(builder, connector);
      }
    }

    // fallback if no connectors are configured
    else {
      builder.addHttpListener(8080, "localhost");
    }

  }

  private void addConnector(Undertow.Builder builder, UndertowConnectorConfig connector) {

    String host = "localhost";
    if (connector.getHost() != null && connector.getHost().trim().length() > 0) {
      host = connector.getHost().trim();
    }

    if (connector.getType() != null && "http".equalsIgnoreCase(connector.getType().trim())) {
      int port = connector.getPort() > 0 ? connector.getPort() : 8080;
      builder.addHttpListener(port, host);
    }

    else if (connector.getType() != null && "ajp".equalsIgnoreCase(connector.getType().trim())) {
      int port = connector.getPort() > 0 ? connector.getPort() : 8009;
      builder.addAjpListener(port, host);
    }

    else {
      throw new IllegalStateException("Unsupported connector type: " + connector.getType());
    }

  }

  public void stop() {

    while (startedModules.size() > 0) {
      Module module = startedModules.pop();
      log.debug("Stopping module: {}", module.getClass().getName());
      module.destroy();
    }

    log.info("Stopping Undertow...");
    server.stop();
    log.info("Shutdown complete!");

  }

  public static class Builder {

    private String classpathPrefix = "webapp";

    private ModuleProvider moduleProvider = new ServiceLoaderModuleProvider();

    private ConfigManager configManager = null;

    private Builder() {
    }

    public Builder classpathPrefix(String classpathPrefix) {
      this.classpathPrefix = classpathPrefix;
      return this;
    }

    public Builder moduleProvider(ModuleProvider moduleProvider) {
      this.moduleProvider = moduleProvider;
      return this;
    }

    public Builder configManager(ConfigManager configManager) {
      this.configManager = configManager;
      return this;
    }

    public Backset build() {
      if (configManager == null) {
        throw new IllegalStateException("ConfigManager is required");
      }
      return new Backset(this);
    }

  }

}
