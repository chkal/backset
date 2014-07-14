package de.chkal.backset.module.myfaces.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.chkal.backset.module.test.BacksetBundleBuilder;
import de.chkal.backset.module.test.http.Request;
import de.chkal.backset.module.test.http.Response;

@RunWith(Arquillian.class)
public class FacesBeanAnnotationsTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addClass(SimpleFacesBean.class)
        .addAsResource(
            new StringAsset("<html>#{simpleFacesBean.value}</html>"),
            "webapp/index.xhtml");

    return BacksetBundleBuilder.create(archive)
        .withMyFacesModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldSupportManagedBeanAnnotations() throws IOException {

    Response response = Request.get(baseUrl, "/index.jsf").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("Hey! I'm a managed bean!");

  }

}
