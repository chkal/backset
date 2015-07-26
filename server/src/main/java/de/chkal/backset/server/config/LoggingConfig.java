package de.chkal.backset.server.config;

import java.util.HashMap;
import java.util.Map;

import de.chkal.backset.module.api.Section;

@Section("logging")
public class LoggingConfig {

  private String level = "INFO";

  private String pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

  private Map<String, String> loggers = new HashMap<>();;

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public Map<String, String> getLoggers() {
    return loggers;
  }

  public void setLoggers(Map<String, String> packages) {
    this.loggers = packages;
  }

}
