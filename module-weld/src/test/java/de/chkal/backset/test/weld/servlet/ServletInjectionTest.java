package de.chkal.backset.test.weld.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.test.BacksetBundleBuilder;
import de.chkal.backset.module.test.http.Request;
import de.chkal.backset.module.test.http.Response;

@RunWith(Arquillian.class)
public class ServletInjectionTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("backset.yml")
        .addClass(ServletInjectionBean.class)
        .addClass(ServletInjectionServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .withWeldModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldInjectDependenciesIntoServlets() throws IOException {

    Response response = Request.get(baseUrl, "/injection").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent())
        .contains("Some CDI bean was injected");

  }

  @Test
  public void shouldCallPostConstructOnServlet() throws IOException {

    Response response = Request.get(baseUrl, "/injection").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent())
        .contains("@PostConstruct called");

  }

}
