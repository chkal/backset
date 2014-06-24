package de.chkal.backset.module.owb;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import org.apache.webbeans.servlet.WebBeansConfigurationListener;

import de.chkal.backset.module.api.DeploymentEnricher;

public class OpenWebBeansDeploymentEnricher implements DeploymentEnricher {

  @Override
  public void enrich(DeploymentInfo deployment) {
    deployment
        .addListener(
            Servlets.listener(WebBeansConfigurationListener.class));
  }

}
