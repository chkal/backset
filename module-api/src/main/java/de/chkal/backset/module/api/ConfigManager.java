package de.chkal.backset.module.api;

public interface ConfigManager {

  <T> T getConfig(String name, Class<T> type);

}