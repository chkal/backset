package de.chkal.backset.module.servlet.annotation;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;

import java.util.UUID;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.servlet.ServletEnricherContext;

public class WebFilterDeploymentEnricher implements DeploymentEnricher {

  private final Logger log = LoggerFactory.getLogger(WebListenerDeploymentEnricher.class);

  private final AnnotationDatabase annotationDatabase;

  private final ServletEnricherContext enricherContext;

  public WebFilterDeploymentEnricher(ModuleContext context, ServletEnricherContext enricherContext) {
    this.enricherContext = enricherContext;
    this.annotationDatabase = context.getAnnotationDatabase();
  }

  @Override
  public int getPriority() {
    return 1020;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {

    if (!enricherContext.isMetadataComplete()) {

      for (Class<?> clazz : annotationDatabase.getTypes(WebFilter.class)) {

        if (Filter.class.isAssignableFrom(clazz)) {

          WebFilter annotation = clazz.getAnnotation(WebFilter.class);
          if (annotation != null) {

            log.debug("Registering filter: {}", clazz.getName());
            FilterInfo filterInfo = createFilterInfo((Class<? extends Filter>) clazz, annotation, factory);

            deployment.addFilter(filterInfo);

            for (String urlPattern : annotation.urlPatterns()) {
              for (DispatcherType dispatcherType : annotation.dispatcherTypes()) {
                deployment.addFilterUrlMapping(filterInfo.getName(), urlPattern, dispatcherType);
              }
            }

            for (String urlPattern : annotation.value()) {
              for (DispatcherType dispatcher : annotation.dispatcherTypes()) {
                deployment.addFilterUrlMapping(filterInfo.getName(), urlPattern, dispatcher);
              }
            }

            for (String servletName : annotation.servletNames()) {
              for (DispatcherType dispatcher : annotation.dispatcherTypes()) {
                deployment.addFilterServletNameMapping(filterInfo.getName(), servletName, dispatcher);
              }
            }

          }

        }

      }

    }

  }

  private FilterInfo createFilterInfo(Class<? extends Filter> clazz, WebFilter annotation,
      InstanceFactoryFactory factory) {

    String name = annotation.filterName();
    if (name.trim().length() == 0) {
      name = UUID.randomUUID().toString();
    }

    FilterInfo filterInfo = new FilterInfo(name, clazz, factory.getInstanceFactory(clazz));
    filterInfo.setAsyncSupported(annotation.asyncSupported());

    for (WebInitParam param : annotation.initParams()) {
      filterInfo.addInitParam(param.name(), param.value());
    }

    return filterInfo;

  }
}
