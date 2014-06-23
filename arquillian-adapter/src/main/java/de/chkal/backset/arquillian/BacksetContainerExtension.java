package de.chkal.backset.arquillian;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class BacksetContainerExtension implements LoadableExtension {

  @Override
  public void register(ExtensionBuilder builder) {
    builder.service(DeployableContainer.class, BacksetDeployableContainer.class);
  }

}
