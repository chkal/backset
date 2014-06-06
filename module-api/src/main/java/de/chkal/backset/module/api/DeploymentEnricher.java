package de.chkal.backset.module.api;

import io.undertow.servlet.api.DeploymentInfo;

public interface DeploymentEnricher {

  void enrich(DeploymentInfo deployment);

}
