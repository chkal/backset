package de.chkal.backset.jersey.simple;

import de.chkal.backset.module.test.BacksetBundleBuilder;
import de.chkal.backset.module.test.http.Request;
import de.chkal.backset.module.test.http.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class SimpleJerseyTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("backset.yml")
        .addClass(SimpleApplication.class)
        .addClass(SimpleResource.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .withJerseyModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldDeploySimpleResources() throws IOException {

    Response response = Request.get(baseUrl, "/r/hello?name=world").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("Hello world");

  }

}
