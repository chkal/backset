package de.chkal.backset.test.weld.multibda;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/multibda")
public class MultipleBDAsServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    MultipleBDAsBean bean = CDI.current().select(MultipleBDAsBean.class).get();

    resp.getOutputStream().println(bean.getResult());

  }

}
