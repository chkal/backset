package de.chkal.backset.core.lifecycle;

import de.chkal.backset.module.api.InjectionProvider;
import de.chkal.backset.module.api.LifecycleProvider;

public interface LifecycleContext {

  Iterable<InjectionProvider> getInjectionProviders();

  Iterable<LifecycleProvider> getLifecycleProviders();

}
