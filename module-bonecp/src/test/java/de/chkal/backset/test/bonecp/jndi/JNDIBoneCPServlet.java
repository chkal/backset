package de.chkal.backset.test.bonecp.jndi;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/bonecp")
public class JNDIBoneCPServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    try {

      InitialContext context = new InitialContext();
      DataSource dataSource = (DataSource) context.lookup("java:/comp/env/TestDataSource");
      resp.getOutputStream().println("Found: " + dataSource.getClass().getName());

    } catch (NamingException e) {
      resp.getOutputStream().println(e.getMessage());
    }

  }
}
