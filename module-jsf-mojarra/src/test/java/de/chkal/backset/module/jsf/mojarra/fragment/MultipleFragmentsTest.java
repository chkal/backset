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
        .withJsfMojarraModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldSupportMultipleFacesConfigFragments() throws IOException {

    Response response = Request.get(baseUrl, "/index.jsf").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent())
        .contains("Fragment 1 got processed")
        .contains("Fragment 2 got processed");

  }

}
