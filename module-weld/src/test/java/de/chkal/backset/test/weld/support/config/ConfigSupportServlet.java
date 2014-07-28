package de.chkal.backset.test.weld.support.config;

import java.io.IOException;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/config-support")
public class ConfigSupportServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    ConfigSupportBean bean = CDI.current().select(ConfigSupportBean.class).get();

    if (bean.getConfigManager() != null) {
      resp.getOutputStream().println("ConfigManager was injected");
    } else {
      resp.getOutputStream().println("ConfigManager was NOT injected");
    }

  }

}
