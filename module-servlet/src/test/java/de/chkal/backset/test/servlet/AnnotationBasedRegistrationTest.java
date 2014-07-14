package de.chkal.backset.test.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.test.BacksetBundleBuilder;

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

    String url = baseUrl.toString() + "/test";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content).contains("AnnotatedHttpServlet is active");

  }

  @Test
  public void shouldFindAnnotatedFilters() throws IOException {

    String url = baseUrl.toString() + "/test";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content).contains("AnnotatedFilter is active");

  }

  @Test
  public void shouldFindAnnotatedListeners() throws IOException {

    String url = baseUrl.toString() + "/test";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content).contains("AnnotatedRequestListener is active");

  }

}
