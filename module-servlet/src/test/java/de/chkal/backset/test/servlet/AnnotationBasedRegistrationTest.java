package de.chkal.backset.test.servlet;

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
public class AnnotationBasedRegistrationTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addClass(AnnotatedHttpServlet.class)
        .addClass(AnnotatedFilter.class)
        .addClass(AnnotatedRequestListener.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldFindAnnotatedServlets() throws IOException {

    Response response = Request.get(baseUrl, "/test").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("AnnotatedHttpServlet is active");

  }

  @Test
  public void shouldFindAnnotatedFilters() throws IOException {

    Response response = Request.get(baseUrl, "/test").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("AnnotatedFilter is active");

  }

  @Test
  public void shouldFindAnnotatedListeners() throws IOException {

    Response response = Request.get(baseUrl, "/test").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("AnnotatedRequestListener is active");

  }

}
