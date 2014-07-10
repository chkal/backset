package de.chkal.backset.module.myfaces.fragment;

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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.test.BacksetBundleBuilder;

@RunWith(Arquillian.class)
@Ignore
public class MultipleFragmentsTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive fragment1 = ShrinkWrap.create(JavaArchive.class, "fragment1.jar")
        .addAsResource("multi-fragment-faces-config-1.xml", "META-INF/faces-config.xml");

    JavaArchive fragment2 = ShrinkWrap.create(JavaArchive.class, "fragment2.jar")
        .addAsResource("multi-fragment-faces-config-2.xml", "META-INF/faces-config.xml");

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("multi-fragment-messages.properties", "Messages.properties")
        .addAsResource(new StringAsset(
            "<html><p>#{msg1['fragment1']}</p><p>#{msg2['fragment2']}</p></html>"),
            "webapp/index.xhtml");

    return BacksetBundleBuilder.create(archive)
        .withArchives(fragment1, fragment2)
        .withMyFacesModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldSupportManagedBeanAnnotations() throws IOException {

    String url = baseUrl.toString() + "/index.jsf";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content)
        .contains("Fragment 1 got processed")
        .contains("Fragment 2 got processed");

  }

}
