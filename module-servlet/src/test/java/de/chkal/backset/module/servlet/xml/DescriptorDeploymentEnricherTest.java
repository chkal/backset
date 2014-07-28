package de.chkal.backset.module.servlet.xml;

import static org.assertj.core.api.Assertions.assertThat;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.FilterMappingInfo;
import io.undertow.servlet.api.FilterMappingInfo.MappingType;
import io.undertow.servlet.api.ServletInfo;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
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

  @Test
  public void shouldRegisterFilters() {

    assertThat(deployment.getFilters())
        .hasSize(1)
        .containsKey("SomeFilter");

    FilterInfo filterInfo = deployment.getFilters().get("SomeFilter");
    assertThat(filterInfo.isAsyncSupported()).isTrue();
    assertThat(filterInfo.getInitParams())
        .hasSize(1)
        .containsEntry("foo", "bar");

    assertThat(deployment.getFilterMappings()).hasSize(2);

    FilterMappingInfo first = deployment.getFilterMappings().get(0);
    assertThat(first.getDispatcher()).isEqualTo(DispatcherType.ERROR);
    assertThat(first.getMappingType()).isEqualTo(MappingType.URL);
    assertThat(first.getMapping()).isEqualTo("*.filter");

    FilterMappingInfo second = deployment.getFilterMappings().get(1);
    assertThat(second.getDispatcher()).isEqualTo(DispatcherType.ERROR);
    assertThat(second.getMappingType()).isEqualTo(MappingType.SERVLET);
    assertThat(second.getMapping()).isEqualTo("SomeServlet");

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

  public static class SomeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    }

    @Override
    public void destroy() {
    }

  }

}
