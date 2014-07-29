package de.chkal.backset.test.servlet.webxml;

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
public class WebXmlRegistrationTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("registration-web.xml", "webapp/WEB-INF/web.xml")
        .addClass(WebXmlFilter.class)
        .addClass(WebXmlServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldRegisterServletFromWebXml() throws IOException {

    Response response = Request.get(baseUrl, "/WebXmlServlet").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("WebXmlServlet has been registered");

  }

  @Test
  public void shouldRegisterFiltersFromWebXml() throws IOException {

    Response response = Request.get(baseUrl, "/WebXmlFilter").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("WebXmlFilter has been registered");

  }
}
