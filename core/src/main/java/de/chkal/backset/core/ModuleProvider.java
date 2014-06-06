package de.chkal.backset.core;

import de.chkal.backset.module.api.Module;

public interface ModuleProvider {

  Iterable<Module> getModules(ClassLoader classloader);
  
}
