package de.chkal.backset.module.test.http;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class Request {

  private HttpUriRequest request;

  public Request(HttpUriRequest request) {
    this.request = request;
  }

  public Response execute() {

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

      return execute(client);

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }

  public Response execute(CloseableHttpClient client) {
    try {
      return new Response(client.execute(request));
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static Request get(URL base, String path) {
    return new Request(new HttpGet(join(base, path)));
  }

  private static String join(URL base, String path) {
    StringBuilder url = new StringBuilder();
    url.append(base.toString());
    if (!url.toString().endsWith("/")) {
      url.append("/");
    }
    if (path.startsWith("/")) {
      url.append(path.substring(1));
    } else {
      url.append(path);
    }
    return url.toString();
  }

}
