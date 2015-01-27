package de.chkal.backset.module.weld;

import de.chkal.backset.module.api.AnnotationDatabase;
import org.jboss.weld.bootstrap.WeldBootstrap;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.manager.api.WeldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Basically something like WeldServletLifecycle
 */
public class WeldBootstrapListener implements ServletContextListener {

  private final Logger log = LoggerFactory.getLogger(WeldBootstrapListener.class);

  public static AnnotationDatabase annotationDatabase = null;

  public static WeldConfig weldConfig = null;

  private Bootstrap bootstrap;

  @Override
  public void contextInitialized(ServletContextEvent event) {

    log.info("Starting up Weld...");

    bootstrap = new WeldBootstrap();

    DeploymentDefinition definition = new DeploymentDefinition(weldConfig, annotationDatabase);

    BacksetDeploymentArchive archive = new BacksetDeploymentArchive(definition);
    BacksetDeployment deployment = new BacksetDeployment(bootstrap, archive);

    bootstrap.startContainer(Environments.SERVLET, deployment);
    bootstrap.startInitialization();
    bootstrap.deployBeans();
    bootstrap.validateBeans();
    bootstrap.endInitialization();

    WeldManager manager = bootstrap.getManager(archive);

    // this is required for the JSF integration
    // see: WeldServletLifecycle.BEAN_MANAGER_ATTRIBUTE_NAME
    event.getServletContext().setAttribute(
        "org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager",
        manager);

  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    bootstrap.shutdown();
    log.info("Weld shutdown complete!");
  }

}
