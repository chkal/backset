package de.chkal.backset.module.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleContext {

  private final List<DeploymentEnricher> deploymentEnrichers = new ArrayList<>();

  public void register(DeploymentEnricher deploymentEnricher) {
    this.deploymentEnrichers.add(deploymentEnricher);
  }

  public List<DeploymentEnricher> getDeploymentEnrichers() {
    return Collections.unmodifiableList(deploymentEnrichers);
  }

}
