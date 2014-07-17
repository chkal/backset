package de.chkal.backset.module.bonecp;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.jolbox.bonecp.BoneCPDataSource;

public class DataSources {

  private static final Map<String, BoneCPDataSource> dataSources = new HashMap<>();

  public static DataSource get() {

    if (dataSources.isEmpty()) {
      throw new IllegalStateException("There are no datasources configured!");
    }

    if (dataSources.size() > 1) {
      throw new IllegalStateException(
          "There is more than one datasource. Please use get(String) instead.");
    }

    return dataSources.values().iterator().next();

  }

  public static DataSource get(String name) {
    return dataSources.get(name);
  }

  protected static void add(String name, BoneCPDataSource dataSource) {
    dataSources.put(name, dataSource);
  }

  protected static Iterable<BoneCPDataSource> getAll() {
    return dataSources.values();
  }

}
