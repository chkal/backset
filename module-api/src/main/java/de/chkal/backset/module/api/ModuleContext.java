package de.chkal.backset.module.api;

public interface ModuleContext {

  void register(DeploymentEnricher deploymentEnricher);

  void register(InjectionProvider injectionProvider);

  void register(LifecycleProvider lifecycleProvider);

  AnnotationDatabase getAnnotationDatabase();

  ConfigManager getConfigManager();

}