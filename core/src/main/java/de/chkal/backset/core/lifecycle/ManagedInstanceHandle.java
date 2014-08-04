package de.chkal.backset.core.lifecycle;

import io.undertow.servlet.api.InstanceHandle;
import de.chkal.backset.module.api.InjectionProvider;
import de.chkal.backset.module.api.LifecycleProvider;

public class ManagedInstanceHandle<T> implements InstanceHandle<T> {

  private final T instance;
  private final LifecycleContext context;

  public ManagedInstanceHandle(T instance, LifecycleContext context) {
    this.instance = instance;
    this.context = context;
  }

  @Override
  public T getInstance() {

    for (InjectionProvider injectionProvider : context.getInjectionProviders()) {
      injectionProvider.inject(instance);
    }

    for (LifecycleProvider injectionProvider : context.getLifecycleProviders()) {
      injectionProvider.init(instance);
    }

    return instance;

  }

  @Override
  public void release() {

    for (LifecycleProvider injectionProvider : context.getLifecycleProviders()) {
      injectionProvider.destroy(instance);
    }

  }

}
