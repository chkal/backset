package de.chkal.backset.core;

import java.util.ServiceLoader;

import de.chkal.backset.module.api.Module;

public class ServiceLoaderModuleProvider implements ModuleProvider {

  @Override
  public Iterable<Module> getModules(ClassLoader classloader) {
    return ServiceLoader.load(Module.class, classloader);
  }

}
