package de.chkal.backset.test.weld.basic;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCDIServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    SimpleCDIBean bean = CDI.current().select(SimpleCDIBean.class).get();

    resp.getOutputStream().println(bean.getMessage());

  }

}
