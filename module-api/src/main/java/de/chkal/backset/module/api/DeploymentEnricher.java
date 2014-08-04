package de.chkal.backset.module.api;

import io.undertow.servlet.api.DeploymentInfo;

public interface DeploymentEnricher extends Ordered {

  void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory);

}
