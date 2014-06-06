package de.chkal.backset.module.api;

public interface Module {

  void init(ModuleContext context);

  void destroy();

}
