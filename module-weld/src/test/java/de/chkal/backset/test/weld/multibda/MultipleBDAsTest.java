package de.chkal.backset.test.weld.multibda;

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
public class MultipleBDAsTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive bda1 = ShrinkWrap.create(JavaArchive.class, "bda1.jar")
        .addClasses(First.class, FirstInterceptor.class)
        .addAsResource("multi-bda-first-beans.xml", "META-INF/beans.xml");

    JavaArchive bda2 = ShrinkWrap.create(JavaArchive.class, "bda2.jar")
        .addClasses(Second.class, SecondInterceptor.class)
        .addAsResource("multi-bda-second-beans.xml", "META-INF/beans.xml");

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("backset.yml")
        .addClass(MultipleBDAsBean.class)
        .addClass(MultipleBDAsServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withArchives(bda1, bda2)
        .withServletModule()
        .withWeldModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldSupportMultipleBeanXmlFiles() throws IOException {

    Response response = Request.get(baseUrl, "/multibda").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent())
        .contains("raw-result")
        .contains("FirstInterceptor")
        .contains("SecondInterceptor");

  }

}
