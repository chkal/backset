package de.chkal.backset.module.weld;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import org.jboss.weld.servlet.WeldInitialListener;

import de.chkal.backset.module.api.DeploymentEnricher;

public class WeldDeploymentEnricher implements DeploymentEnricher {

  @Override
  public void enrich(DeploymentInfo deployment) {

    deployment

        /*
         * Our custom initialization class which does a similar job as
         * "org.jboss.weld.environment.servlet.Listener"
         */
        .addListener(Servlets.listener(WeldBootstrapListener.class))

        /*
         * Weld listener that activates contexts
         */
        .addListener(Servlets.listener(WeldInitialListener.class));
  }

}
