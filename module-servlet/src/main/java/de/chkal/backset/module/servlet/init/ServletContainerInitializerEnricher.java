package de.chkal.backset.module.servlet.init;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.annotation.HandlesTypes;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

public class ServletContainerInitializerEnricher implements DeploymentEnricher {

  private static final Logger log = LoggerFactory.getLogger(ServletContainerInitializerEnricher.class);

  private final AnnotationDatabase annotationDatabase;

  public ServletContainerInitializerEnricher(AnnotationDatabase annotationDatabase) {
    this.annotationDatabase = annotationDatabase;
  }

  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {

    // Dirty (but simple) way of getting the implementation classes
    for (ServletContainerInitializer initializer : ServiceLoader.load(ServletContainerInitializer.class)) {

      Class<? extends ServletContainerInitializer> clazz = initializer.getClass();

      log.debug("Registering ServletContainerInitializer: {}", clazz.getName());

      deployment.addServletContainerInitalizer(buildInitializerInfo(factory, clazz));

    }

  }

  private ServletContainerInitializerInfo buildInitializerInfo(InstanceFactoryFactory factory,
      Class<? extends ServletContainerInitializer> clazz) {

    Set<Class<?>> types = new HashSet<>();

    HandlesTypes handlesTypes = clazz.getAnnotation(HandlesTypes.class);
    if (handlesTypes != null) {
      for (Class<?> type : handlesTypes.value()) {
        if (type.isAnnotation()) {
          types.addAll(annotationDatabase.getTypes((Class<? extends Annotation>) type));
        }
      }
    }

    InstanceFactory<? extends ServletContainerInitializer> instanceFactory = factory.getInstanceFactory(clazz);

    return new ServletContainerInitializerInfo(clazz, instanceFactory, types);

  }

  @Override
  public int getPriority() {
    return 990;
  }

}
