package de.chkal.backset.module.weld;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import org.jboss.weld.servlet.WeldInitialListener;

import java.util.HashSet;

public class WeldDeploymentEnricher implements DeploymentEnricher {

  @Override
  public int getPriority() {
    return 100;
  }
  
  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {

    deployment

        /*
         * Our custom initialization class which does a similar job as
         * "org.jboss.weld.environment.servlet.Listener"
         */
        .addServletContainerInitalizers(
            new ServletContainerInitializerInfo(
                WeldBootstrapInitializer.class,
                factory.getInstanceFactory(WeldBootstrapInitializer.class),
                new HashSet<Class<?>>()
            )
        )

        /*
         * Weld listener that activates contexts
         */
        .addListener(Servlets.listener(WeldInitialListener.class));
  }

}
