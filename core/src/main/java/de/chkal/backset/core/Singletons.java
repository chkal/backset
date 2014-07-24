package de.chkal.backset.core;

import java.util.HashMap;
import java.util.Map;

public class Singletons {

  private static final Map<Class<?>, Object> instances = new HashMap<>();

  public static <T> void register(Class<T> type, T instance) {
    Object existing = instances.put(type, instance);
    if (existing != null) {
      throw new IllegalStateException("There is already an instance of: " + type.getName());
    }
  }

  public static <T> T get(Class<T> type) {
    Object instance = instances.get(type);
    if (instance != null) {
      return type.cast(instance);
    }
    return null;
  }

}
