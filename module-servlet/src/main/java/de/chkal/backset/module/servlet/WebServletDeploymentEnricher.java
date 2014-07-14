package de.chkal.backset.module.servlet;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.util.UUID;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.ModuleContext;

public class WebServletDeploymentEnricher implements DeploymentEnricher {

  private final AnnotationDatabase annotationDatabase;

  public WebServletDeploymentEnricher(ModuleContext context) {
    this.annotationDatabase = context.getAnnotationDatabase();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void enrich(DeploymentInfo deployment) {

    for (Class<?> clazz : annotationDatabase.getTypes(WebServlet.class)) {

      if (Servlet.class.isAssignableFrom(clazz)) {

        WebServlet annotation = clazz.getAnnotation(WebServlet.class);
        if (annotation != null) {
          deployment.addServlet(createServletInfo((Class<? extends Servlet>) clazz, annotation));
        }

      }

    }

  }

  private ServletInfo createServletInfo(Class<? extends Servlet> clazz, WebServlet annotation) {

    String name = annotation.name();
    if (name.trim().length() == 0) {
      name = UUID.randomUUID().toString();
    }

    ServletInfo info = new ServletInfo(name, clazz);
    info.addMappings(annotation.urlPatterns());
    info.addMappings(annotation.value());

    info.setAsyncSupported(annotation.asyncSupported());
    info.setLoadOnStartup(annotation.loadOnStartup());

    for (WebInitParam param : annotation.initParams()) {
      info.addInitParam(param.name(), param.value());
    }

    return info;

  }
}
