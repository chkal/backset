package de.chkal.backset.module.weld;

import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jboss.weld.bootstrap.WeldBootstrap;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.environment.servlet.WeldServletLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.AnnotationDatabase;

/**
 * Basically something like {@link WeldServletLifecycle}
 */
public class WeldBootstrapListener implements ServletContextListener {

  private final Logger log = LoggerFactory.getLogger(WeldBootstrapListener.class);

  public static AnnotationDatabase annotationDatabase = null;

  private Bootstrap bootstrap;

  @Override
  public void contextInitialized(ServletContextEvent event) {

    log.info("Starting up Weld...");

    bootstrap = new WeldBootstrap();

    Set<String> beanClasses = annotationDatabase.getTypeNamesByPackage("de.chkal.backset");

    BacksetDeploymentArchive archive = new BacksetDeploymentArchive(beanClasses);
    BacksetDeployment deployment = new BacksetDeployment(bootstrap, archive);

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
