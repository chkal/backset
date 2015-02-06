package de.chkal.backset.module.weld;

import de.chkal.backset.module.api.AnnotationDatabase;
import org.jboss.weld.bootstrap.WeldBootstrap;
import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.Environments;
import org.jboss.weld.manager.api.WeldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

/**
 * Basically something like WeldServletLifecycle
 */
public class WeldBootstrapInitializer implements ServletContainerInitializer {

  private final Logger log = LoggerFactory.getLogger(WeldBootstrapInitializer.class);

  public static AnnotationDatabase annotationDatabase = null;

  public static WeldConfig weldConfig = null;

  private static boolean initialized = false;

  private Bootstrap bootstrap;

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {

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
    servletContext.setAttribute(
        "org.jboss.weld.environment.servlet.javax.enterprise.inject.spi.BeanManager",
        manager);

    initialized = true;

  }

  public static boolean isInitialized() {
    return initialized;
  }

}
