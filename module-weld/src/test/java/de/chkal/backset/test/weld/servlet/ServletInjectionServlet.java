package de.chkal.backset.test.weld.servlet;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/injection")
public class ServletInjectionServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private ServletInjectionBean bean;

  private boolean postConstructCalled = false;

  @PostConstruct
  public void postConstruct() {
    postConstructCalled = true;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    if (bean != null) {
      resp.getOutputStream().println(bean.getName() + " was injected");
    } else {
      resp.getOutputStream().println("Nothing was injected");
    }

    if (postConstructCalled) {
      resp.getOutputStream().println("@PostConstruct called");
    } else {
      resp.getOutputStream().println("@PostConstruct NOT called");
    }

  }

}
