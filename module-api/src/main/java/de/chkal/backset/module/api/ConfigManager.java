package de.chkal.backset.module.api;

public interface ConfigManager {

  <T> T getConfig(Class<T> type);

}