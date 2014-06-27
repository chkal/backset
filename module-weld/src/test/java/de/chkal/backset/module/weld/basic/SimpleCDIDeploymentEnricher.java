package de.chkal.backset.module.weld.basic;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import de.chkal.backset.module.api.DeploymentEnricher;

public class SimpleCDIDeploymentEnricher implements DeploymentEnricher {

  @Override
  public void enrich(DeploymentInfo deployment) {
    deployment.addServlet(
        Servlets.servlet("SimpleCDIServlet", SimpleCDIServlet.class)
            .addMapping("/cdi"));
  }
}
