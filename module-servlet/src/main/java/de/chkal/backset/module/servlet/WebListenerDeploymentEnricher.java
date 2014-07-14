package de.chkal.backset.module.servlet;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ListenerInfo;

import java.util.EventListener;

import javax.servlet.annotation.WebListener;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.ModuleContext;

public class WebListenerDeploymentEnricher implements DeploymentEnricher {

  private final AnnotationDatabase annotationDatabase;

  public WebListenerDeploymentEnricher(ModuleContext context) {
    this.annotationDatabase = context.getAnnotationDatabase();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void enrich(DeploymentInfo deployment) {

    for (Class<?> clazz : annotationDatabase.getTypes(WebListener.class)) {

      if (EventListener.class.isAssignableFrom(clazz)) {

        deployment.addListener(new ListenerInfo((Class<? extends EventListener>) clazz));

      }

    }

  }

}
