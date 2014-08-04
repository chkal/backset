package de.chkal.backset.module.api;

import io.undertow.servlet.api.InstanceFactory;

public interface InstanceFactoryFactory {

  <T> InstanceFactory<T> getInstanceFactory(Class<T> type);
}
