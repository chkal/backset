package de.chkal.backset.jersey.cdi;

import de.chkal.backset.module.test.BacksetBundleBuilder;
import de.chkal.backset.module.test.http.Request;
import de.chkal.backset.module.test.http.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class CDIIntegrationTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml")
        .addAsResource("backset.yml")
        .addClass(CIDBean.class)
        .addClass(CDIApplication.class)
        .addClass(CDIResource.class);

    return BacksetBundleBuilder.create(archive)
        .withServletModule()
        .withWeldModule()
        .withJerseyModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldLookupCDIBean() throws IOException {

    Response response = Request.get(baseUrl, "/r/cdi/lookup").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("I'm a CDI bean");

  }

  @Test
  public void shouldInjectCDIBean() throws IOException {

    Response response = Request.get(baseUrl, "/r/cdi/inject").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("I'm a CDI bean");

  }

}
