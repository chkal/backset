package de.chkal.backset.core.lifecycle;

import io.undertow.servlet.api.InstanceFactory;
import de.chkal.backset.module.api.InstanceFactoryFactory;

public class DefaultInstanceFactoryFactory implements InstanceFactoryFactory {

  private final LifecycleContext context;

  public DefaultInstanceFactoryFactory(LifecycleContext context) {
    this.context = context;
  }

  @Override
  public <T> InstanceFactory<T> getInstanceFactory(Class<T> type) {
    return new ConstructorInstanceFactory<>(type, context);
  }

}
