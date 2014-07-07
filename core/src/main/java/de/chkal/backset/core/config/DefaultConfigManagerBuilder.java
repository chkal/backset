package de.chkal.backset.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.io.Closeables;

public class DefaultConfigManagerBuilder {

  private final ClassLoader classLoader;

  private final DefaultConfigManager configManager;

  public DefaultConfigManagerBuilder() {
    classLoader = Thread.currentThread().getContextClassLoader();
    configManager = new DefaultConfigManager();
  }

  public DefaultConfigManagerBuilder addConfigFile(File file) {

    if (file.exists() && file.isFile() && file.canRead()) {

      FileInputStream stream = null;
      try {
        
        stream = new FileInputStream(file);
        configManager.addConfig(stream);
        return this;

      } catch (IOException e) {
        Closeables.closeQuietly(stream);
      }

    }
    throw new IllegalArgumentException("Cannot read config file: " + file.getAbsolutePath());

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
