package de.chkal.backset.module.weld.basic;

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
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.test.BacksetBundleBuilder;

@RunWith(Arquillian.class)
public class SimpleCDITest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource(EmptyAsset.INSTANCE, "META-INF/beans.xml")
        .addAsResource("backset.yml")
        .addClass(SimpleCDIServlet.class)
        .addClass(SimpleCDIModule.class)
        .addClass(SimpleCDIDeploymentEnricher.class)
        .addClass(SimpleCDIBean.class)
        .addAsServiceProvider(Module.class, SimpleCDIModule.class);

    return BacksetBundleBuilder.create(archive)
        .withWeldModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void weldShouldFindManagedBean() throws IOException {

    String url = baseUrl.toString() + "/cdi";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content).contains("I'm a CDI bean");

  }

}