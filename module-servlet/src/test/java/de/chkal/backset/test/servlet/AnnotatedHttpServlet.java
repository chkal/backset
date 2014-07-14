package de.chkal.backset.test.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/test")
public class AnnotatedHttpServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    resp.getOutputStream().println("AnnotatedHttpServlet is active");

    String filterMsg = (String) req.getAttribute(AnnotatedFilter.class.getName());
    if (filterMsg != null) {
      resp.getOutputStream().println(filterMsg);
    }

    String listenerMsg = (String) req.getAttribute(AnnotatedRequestListener.class.getName());
    if (listenerMsg != null) {
      resp.getOutputStream().println(listenerMsg);
    }

  }

}
