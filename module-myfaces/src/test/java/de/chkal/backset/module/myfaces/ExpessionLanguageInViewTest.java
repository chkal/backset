package de.chkal.backset.module.myfaces;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.test.Deployments;

@RunWith(Arquillian.class)
public class ExpessionLanguageInViewTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {
    return ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .merge(Deployments.getBacksetBase())
        .merge(Deployments.getBacksetMyFaces())
        .addAsResource(
            new StringAsset("<html>1 + 2 = #{1+2}</html>"),
            "webapp/index.xhtml");
  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldEvaluateELExpressions() throws IOException {

    String url = baseUrl.toString() + "/index.jsf";

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet get = new HttpGet(url);
    CloseableHttpResponse response = client.execute(get);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

    String content = EntityUtils.toString(response.getEntity());
    assertThat(content).contains("1 + 2 = 3");

  }

}
