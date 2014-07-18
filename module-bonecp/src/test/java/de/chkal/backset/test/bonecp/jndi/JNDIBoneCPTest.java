package de.chkal.backset.test.bonecp.jndi;

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
public class JNDIBoneCPTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("jndi-backset.yml", "backset.yml")
        .addClass(JNDIBoneCPServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .withJNDIModule()
        .withBoneCPModule()
        .withMavenDependency("com.h2database:h2")
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldBeAbleToUseDataSource() throws IOException {

    Response response = Request.get(baseUrl, "/bonecp").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("Found: com.jolbox.bonecp.BoneCPDataSource");

  }

}
