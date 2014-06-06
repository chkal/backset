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

public class Backset {

  private final Logger log = LoggerFactory.getLogger(Backset.class);

  private final ClassLoader classLoader = Backset.class.getClassLoader();

  private Undertow server;

  private final String classpathPrefix;

  public static Builder builder() {
    return new Builder();
  }

  public Backset(Builder builder) {
    this.classpathPrefix = builder.classpathPrefix;
  }

  public void start() {

    try {

      ClassPathResourceManager resourceManager =
          new ClassPathResourceManager(classLoader, classpathPrefix);

      DeploymentInfo servletBuilder = Servlets.deployment()
          .setClassLoader(classLoader)
          .setResourceManager(resourceManager)
          .setContextPath("/")
          .setDeploymentName("deployment.war");

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

    private Builder() {
    }

    protected Builder classpathPrefix(String classpathPrefix) {
      this.classpathPrefix = classpathPrefix;
      return this;
    }

    public Backset build() {
      return new Backset(this);
    }

  }

}
