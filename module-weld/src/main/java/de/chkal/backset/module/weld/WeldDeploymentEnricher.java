package de.chkal.backset.module.weld;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import de.chkal.backset.module.api.DeploymentEnricher;

public class WeldDeploymentEnricher implements DeploymentEnricher {

  @Override
  public void enrich(DeploymentInfo deployment) {
    deployment
        .addListener(Servlets.listener(WeldBootstrapListener.class));
  }

}
