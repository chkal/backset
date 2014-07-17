package de.chkal.backset.test.bonecp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import de.chkal.backset.module.bonecp.DataSources;

@WebServlet("/bonecp")
public class SimpleBoneCPServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    DataSource dataSource = DataSources.get();
    if (dataSource == null) {
      resp.getOutputStream().println("DataSource not found");
      return;
    }

    try (Connection connection = dataSource.getConnection()) {

      String productName = connection.getMetaData().getDatabaseProductName();
      resp.getOutputStream().println("DatabaseProductName: " + productName);
      return;

    } catch (SQLException e) {
      resp.getOutputStream().println(e.getMessage());
      return;
    }

  }

}
