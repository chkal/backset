package de.chkal.backset.module.servlet.xml;

import static org.assertj.core.api.Assertions.assertThat;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.io.InputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

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

  @Test
  public void shouldRegisterServlets() {

    assertThat(deployment.getServlets())
        .hasSize(1)
        .containsKey("SomeServlet");

    ServletInfo servletInfo = deployment.getServlets().get("SomeServlet");
    assertThat(servletInfo.getServletClass()).isEqualTo(SomeServlet.class);
    assertThat(servletInfo.isAsyncSupported()).isTrue();
    assertThat(servletInfo.getLoadOnStartup()).isEqualTo(5);
    assertThat(servletInfo.getInitParams())
        .hasSize(1)
        .containsEntry("foo", "bar");
    assertThat(servletInfo.getMappings())
        .hasSize(1)
        .contains("*.test");

  }

  public static class SomeListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

  }

  public static class SomeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // nothing

  }

}
