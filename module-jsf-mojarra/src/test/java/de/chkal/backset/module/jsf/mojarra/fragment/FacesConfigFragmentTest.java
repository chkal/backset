package de.chkal.backset.module.jsf.mojarra.fragment;

import java.io.IOException;
import java.net.URL;

import de.chkal.backset.module.test.BacksetBundleBuilder;
import de.chkal.backset.module.test.http.Request;
import de.chkal.backset.module.test.http.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
public class FacesConfigFragmentTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addAsResource("fragment-faces-config.xml", "META-INF/faces-config.xml")
        .addAsResource("fragment-messages.properties", "Messages.properties")
        .addAsResource(
            new StringAsset("<html>#{msg['some-message']}</html>"),
            "webapp/index.xhtml");

    return BacksetBundleBuilder.create(archive)
        .withJsfMojarraModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldProcessFacesConfigFragments() throws IOException {

    Response response = Request.get(baseUrl, "/index.jsf").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("Text from a message bundle");

  }

}
