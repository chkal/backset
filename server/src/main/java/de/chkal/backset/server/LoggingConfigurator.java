package de.chkal.backset.server;

import java.util.Map.Entry;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import de.chkal.backset.server.config.LoggingConfig;

public class LoggingConfigurator {

  private static final String PATTERN = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n";

  private LoggingConfig config = new LoggingConfig();

  public void configure() {

    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

    LevelChangePropagator levelChangePropagator = new LevelChangePropagator();
    levelChangePropagator.setResetJUL(true);
    levelChangePropagator.setContext(context);
    context.addListener(levelChangePropagator);

    PatternLayoutEncoder layout = new PatternLayoutEncoder();
    layout.setContext(context);
    layout.setPattern(PATTERN);
    layout.start();

    ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<ILoggingEvent>();
    appender.setContext(context);
    appender.setName("console");
    appender.setEncoder(layout);
    appender.start();

    Level rootLevel = Level.INFO;
    if (config != null && config.getLevel() != null) {
      rootLevel = parseLevel(config.getLevel());
    }

    Logger root = context.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(rootLevel);
    root.addAppender(appender);

    if (config != null && config.getLoggers() != null) {
      for (Entry<String, String> entry : config.getLoggers().entrySet()) {
        Logger logger = context.getLogger(entry.getKey());
        logger.setLevel(parseLevel(entry.getValue()));
      }
    }

  }

  private Level parseLevel(String name) {
    if (name != null && name.trim().length() > 0) {
      Level level = Level.toLevel(name.toUpperCase().trim(), null);
      if (level != null) {
        return level;
      }
    }
    throw new IllegalStateException("Invalid log level: " + name);

  }

  public LoggingConfig getConfig() {
    return config;
  }

  public void setConfig(LoggingConfig config) {
    this.config = config;
  }

}
