package de.chkal.backset.module.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleContext {

  private final List<DeploymentEnricher> deploymentEnrichers = new ArrayList<>();
  private AnnotationDatabase annotationDatabase;

  public ModuleContext(AnnotationDatabase annotationDatabase) {
    this.annotationDatabase = annotationDatabase;
  }

  public void register(DeploymentEnricher deploymentEnricher) {
    this.deploymentEnrichers.add(deploymentEnricher);
  }

  public List<DeploymentEnricher> getDeploymentEnrichers() {
    return Collections.unmodifiableList(deploymentEnrichers);
  }

  public AnnotationDatabase getAnnotationDatabase() {
    return annotationDatabase;
  }

}
