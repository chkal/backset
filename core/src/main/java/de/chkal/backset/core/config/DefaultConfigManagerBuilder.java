package de.chkal.backset.core.config;

import java.io.IOException;
import java.io.InputStream;

public class DefaultConfigManagerBuilder {

  private final ClassLoader classLoader;

  private final DefaultConfigManager configManager;

  public DefaultConfigManagerBuilder() {
    classLoader = Thread.currentThread().getContextClassLoader();
    configManager = new DefaultConfigManager();
  }

  public DefaultConfigManagerBuilder addClasspathConfig(String resource) {

    try {

      InputStream stream = classLoader.getResourceAsStream(resource);
      if (stream != null) {
        configManager.addConfig(stream);
      }
      return this;

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }

  public DefaultConfigManager build() {
    return configManager;
  }

}
