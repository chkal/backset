package de.chkal.backset.module.api;

public interface ModuleContext {

  void register(DeploymentEnricher deploymentEnricher);

  AnnotationDatabase getAnnotationDatabase();

}