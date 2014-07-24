package de.chkal.backset.module.servlet.xml;

import static org.assertj.core.api.Assertions.assertThat;
import io.undertow.servlet.api.DeploymentInfo;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.junit.Before;
import org.junit.Test;

public class DescriptorDeploymentEnricherTest {

  private DeploymentInfo deployment;

  @Before
  public void prepare() {

    deployment = new DeploymentInfo();

    DescriptorDeploymentEnricher enricher = new DescriptorDeploymentEnricher();

    InputStream webXml =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("full-web.xml");
    assertThat(webXml).isNotNull();

    enricher.processWebXml(deployment, webXml);

  }

  @Test
  public void shouldRegisterContextParams() {

    assertThat(deployment.getInitParameters())
        .hasSize(1)
        .containsEntry("some-param-name", "some-param-value");

  }

  @Test
  public void shouldRegisterListeners() {

    assertThat(deployment.getListeners())
        .hasSize(1)
        .extracting("listenerClass")
        .containsExactly(SomeListener.class);

  }

  public static class SomeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

  }

}
