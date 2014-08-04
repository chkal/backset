package de.chkal.backset.module.api;

public interface LifecycleProvider extends Ordered {

  void init(Object o);

  void destroy(Object o);

}
