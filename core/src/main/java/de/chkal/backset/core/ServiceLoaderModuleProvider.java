package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.OrderedComparator;

public class ServiceLoaderModuleProvider implements ModuleProvider {

  @Override
  public Iterable<Module> getModules(ClassLoader classloader) {

    List<Module> modules = new ArrayList<>();

    for (Module module : ServiceLoader.load(Module.class, classloader)) {
      modules.add(module);
    }

    Collections.sort(modules, OrderedComparator.INSTANCE);

    return modules;

  }

}
