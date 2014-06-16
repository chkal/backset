package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.ModuleContext;

public class DefaultModuleContext implements ModuleContext {

  private final List<DeploymentEnricher> deploymentEnrichers = new ArrayList<>();
  private AnnotationDatabase annotationDatabase;

  public DefaultModuleContext(AnnotationDatabase annotationDatabase) {
    this.annotationDatabase = annotationDatabase;
  }

  /* (non-Javadoc)
   * @see de.chkal.backset.module.api.ModuleContext#register(de.chkal.backset.module.api.DeploymentEnricher)
   */
  @Override
  public void register(DeploymentEnricher deploymentEnricher) {
    this.deploymentEnrichers.add(deploymentEnricher);
  }

  public List<DeploymentEnricher> getDeploymentEnrichers() {
    return Collections.unmodifiableList(deploymentEnrichers);
  }

  /* (non-Javadoc)
   * @see de.chkal.backset.module.api.ModuleContext#getAnnotationDatabase()
   */
  @Override
  public AnnotationDatabase getAnnotationDatabase() {
    return annotationDatabase;
  }

}
