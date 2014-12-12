package de.chkal.backset.arquillian;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

public class BacksetContainerConfiguration implements ContainerConfiguration {

  private int port = 48123;

  @Override public void validate() throws ConfigurationException {
    if (port <= 0) {
      throw new ConfigurationException("Invalid port: " + port);
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

}
