package de.chkal.backset.core.lifecycle;

import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.InstanceHandle;

public class ConstructorInstanceFactory<T> implements InstanceFactory<T> {

  private final Class<T> type;
  private final LifecycleContext context;

  public ConstructorInstanceFactory(Class<T> type, LifecycleContext context) {
    this.type = type;
    this.context = context;
  }

  @Override
  public InstanceHandle<T> createInstance() throws InstantiationException {
    return new ManagedInstanceHandle<T>(createInstance(type), context);
  }

  private static <T> T createInstance(Class<T> type) {
    try {
      return type.newInstance();
    } catch (InstantiationException e) {
      throw new IllegalStateException(e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(type.getName() + " has no public default constructor");
    }

  }

}
