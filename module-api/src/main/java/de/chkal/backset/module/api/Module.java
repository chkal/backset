package de.chkal.backset.module.api;

public interface Module extends Ordered {

  void init(ModuleContext context);

  void destroy();

}
