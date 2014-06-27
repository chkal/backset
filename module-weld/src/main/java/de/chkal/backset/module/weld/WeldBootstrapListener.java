package de.chkal.backset.module.weld;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.weld.bootstrap.WeldBootstrap;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.jboss.weld.environment.servlet.deployment.ServletDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Basically something like {@link WeldServletLifecycle}
 */
public class WeldBootstrapListener implements ServletContextListener {

  private final Logger log = LoggerFactory.getLogger(WeldBootstrapListener.class);

  private Bootstrap bootstrap;

  @Override
  public void contextInitialized(ServletContextEvent event) {

    ServletContext context = event.getServletContext();

    log.info("Starting up Weld...");

    bootstrap = new WeldBootstrap();

    ServletDeployment deployment = new ServletDeployment(context, bootstrap, null);

    bootstrap.startContainer(Environments.SERVLET, deployment);
    bootstrap.startInitialization();
    bootstrap.deployBeans();
    bootstrap.validateBeans();
    bootstrap.endInitialization();

  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    bootstrap.shutdown();
    log.info("Weld shutdown complete!");
  }

}
