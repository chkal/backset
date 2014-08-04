package de.chkal.backset.module.servlet.annotation;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ListenerInfo;

import java.util.EventListener;

import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.servlet.ServletEnricherContext;

public class WebListenerDeploymentEnricher implements DeploymentEnricher {

  private final Logger log = LoggerFactory.getLogger(WebListenerDeploymentEnricher.class);

  private final AnnotationDatabase annotationDatabase;

  private final ServletEnricherContext enricherContext;

  public WebListenerDeploymentEnricher(ModuleContext context, ServletEnricherContext enricherContext) {
    this.enricherContext = enricherContext;
    this.annotationDatabase = context.getAnnotationDatabase();
  }

  @Override
  public int getPriority() {
    return 1010;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {

    if (!enricherContext.isMetadataComplete()) {

      for (Class<?> clazz : annotationDatabase.getTypes(WebListener.class)) {

        if (EventListener.class.isAssignableFrom(clazz)) {

          log.debug("Registering listener: {}", clazz.getName());
          Class<? extends EventListener> type = (Class<? extends EventListener>) clazz;
          deployment.addListener(new ListenerInfo(type, factory.getInstanceFactory(type)));

        }

      }

    }

  }

}
