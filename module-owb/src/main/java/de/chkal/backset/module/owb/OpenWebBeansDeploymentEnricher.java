package de.chkal.backset.module.owb;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import org.apache.webbeans.servlet.WebBeansConfigurationListener;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;

public class OpenWebBeansDeploymentEnricher implements DeploymentEnricher {

  @Override
  public int getPriority() {
    return 150;
  }
  
  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {
    deployment
        .addListener(
            Servlets.listener(WebBeansConfigurationListener.class));
  }

}
