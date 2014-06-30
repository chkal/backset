package de.chkal.backset.core;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.core.annotation.ReflectionsAnnotationDatabase;
import de.chkal.backset.core.config.DefaultConfigManager;
import de.chkal.backset.core.config.DefaultConfigManagerBuilder;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.Module;

public class Backset {

  private final Logger log = LoggerFactory.getLogger(Backset.class);

  private final ClassLoader classLoader = Backset.class.getClassLoader();

  private Undertow server;

  private final String classpathPrefix;

  private final ModuleProvider moduleProvider;

  public static Builder builder() {
    return new Builder();
  }

  public Backset(Builder builder) {
    this.classpathPrefix = builder.classpathPrefix;
    this.moduleProvider = builder.moduleProvider;
  }

  public void start() {

    DefaultConfigManager configManager = new DefaultConfigManagerBuilder()
        .addClasspathConfig("backset.yml")
        .build();

    ReflectionsAnnotationDatabase annotationDatabase = new ReflectionsAnnotationDatabase();
    DefaultModuleContext moduleContext =
        new DefaultModuleContext(annotationDatabase, configManager);

    for (Module module : moduleProvider.getModules(classLoader)) {
      log.info("Starting module: {}", module.getClass().getName());
      module.init(moduleContext);
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

      DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
      manager.deploy();
      HttpHandler servletHandler = manager.start();

      server = Undertow.builder()
          .addHttpListener(8080, "localhost")
          .setHandler(servletHandler)
          .build();

      server.start();
      log.info("Backset started!");

    } catch (ServletException e) {
      throw new IllegalStateException(e);
    }

  }

  public void stop() {
    server.stop();
  }

  public static class Builder {

    private String classpathPrefix = "webapp";

    private ModuleProvider moduleProvider = new ServiceLoaderModuleProvider();

    private Builder() {
    }

    protected Builder classpathPrefix(String classpathPrefix) {
      this.classpathPrefix = classpathPrefix;
      return this;
    }

    protected Builder moduleProvider(ModuleProvider moduleProvider) {
      this.moduleProvider = moduleProvider;
      return this;
    }

    public Backset build() {
      return new Backset(this);
    }

  }

}
