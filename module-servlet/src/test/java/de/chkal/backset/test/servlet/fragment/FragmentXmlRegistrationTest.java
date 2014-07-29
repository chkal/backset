package de.chkal.backset.test.servlet.fragment;

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
public class FragmentXmlRegistrationTest {

  @Deployment(testable = false)
  public static JavaArchive createDeployment() {

    JavaArchive fragment1 = ShrinkWrap.create(JavaArchive.class)
        .addAsResource("registration-fragment1.xml", "META-INF/web-fragment.xml");

    JavaArchive fragment2 = ShrinkWrap.create(JavaArchive.class)
        .addAsResource("registration-fragment2.xml", "META-INF/web-fragment.xml");

    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "backset-test.jar")
        .addClass(FragmentXmlFilter.class)
        .addClass(FragmentXmlServlet.class);

    return BacksetBundleBuilder.create(archive)
        .withArchives(fragment1, fragment2)
        .withServletModule()
        .build();

  }

  @ArquillianResource
  private URL baseUrl;

  @Test
  public void shouldRegisterServletFromFirstFragment() throws IOException {

    Response response = Request.get(baseUrl, "/fragment1/FragmentXmlServlet").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("FragmentXmlServlet has been registered");

  }

  @Test
  public void shouldRegisterFilterFromFirstFragment() throws IOException {

    Response response = Request.get(baseUrl, "/fragment1/FragmentXmlFilter").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("FragmentXmlFilter has been registered");

  }

  @Test
  public void shouldRegisterServletFromSecondFragment() throws IOException {

    Response response = Request.get(baseUrl, "/fragment2/FragmentXmlServlet").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("FragmentXmlServlet has been registered");

  }

  @Test
  public void shouldRegisterFilterFromSecondFragment() throws IOException {

    Response response = Request.get(baseUrl, "/fragment2/FragmentXmlFilter").execute();

    assertThat(response.getStatusCode()).isEqualTo(200);
    assertThat(response.getContent()).contains("FragmentXmlFilter has been registered");

  }
}
