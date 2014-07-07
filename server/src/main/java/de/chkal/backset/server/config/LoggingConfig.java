package de.chkal.backset.server.config;

import java.util.HashMap;
import java.util.Map;

public class LoggingConfig {

  public static final String NAME = "logging";

  private String level = "INFO";

  private Map<String, String> loggers = new HashMap<>();;

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public Map<String, String> getLoggers() {
    return loggers;
  }

  public void setLoggers(Map<String, String> packages) {
    this.loggers = packages;
  }

}
