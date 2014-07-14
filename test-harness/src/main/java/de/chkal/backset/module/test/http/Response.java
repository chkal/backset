package de.chkal.backset.module.test.http;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class Response {

  private final CloseableHttpResponse response;
  private final String content;

  public Response(CloseableHttpResponse response) throws IOException {
    this.response = response;
    this.content = EntityUtils.toString(response.getEntity());
  }

  public CloseableHttpResponse getResponse() {
    return response;
  }

  public String getContent() {
    return content;
  }

  public int getStatusCode() {
    return response.getStatusLine().getStatusCode();
  }

}
