package de.chkal.backset.test.weld.alternative;

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
public class AlternativeTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("alternative-second-beans.xml", "META-INF/beans.xml")
        .addAsResource("backset.yml")
        .addClass(AlternativeInterface.class)
        .addClasses(FirstAlternative.class, SecondAlternative.class)
        .addClass(AlternativeServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .withWeldModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void weldShouldFindManagedBean() throws IOException {

    Response response = Request.get(baseUrl, "/alternative").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("Second");

  }

}
