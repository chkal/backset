package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.ModuleContext;

public class DefaultModuleContext implements ModuleContext {

  private final List<DeploymentEnricher> deploymentEnrichers = new ArrayList<>();
  private final AnnotationDatabase annotationDatabase;
  private final ConfigManager configManager;

  public DefaultModuleContext(AnnotationDatabase annotationDatabase, ConfigManager configManager) {
    this.annotationDatabase = annotationDatabase;
    this.configManager = configManager;
  }

  @Override
  public void register(DeploymentEnricher deploymentEnricher) {
    this.deploymentEnrichers.add(deploymentEnricher);
  }

  public List<DeploymentEnricher> getDeploymentEnrichers() {
    return Collections.unmodifiableList(deploymentEnrichers);
  }

  @Override
  public AnnotationDatabase getAnnotationDatabase() {
    return annotationDatabase;
  }

  @Override
  public ConfigManager getConfigManager() {
    return configManager;
  }

}
