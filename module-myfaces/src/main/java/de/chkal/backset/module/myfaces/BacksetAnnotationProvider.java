package de.chkal.backset.module.myfaces;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.component.FacesComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.ExternalContext;
import javax.faces.convert.FacesConverter;
import javax.faces.event.NamedEvent;
import javax.faces.render.FacesBehaviorRenderer;
import javax.faces.render.FacesRenderer;
import javax.faces.validator.FacesValidator;

import org.apache.myfaces.spi.AnnotationProvider;

import de.chkal.backset.module.api.AnnotationDatabase;

public class BacksetAnnotationProvider extends AnnotationProvider {

  private final static Iterable<Class<? extends Annotation>> TYPES = Arrays.asList(
      ManagedBean.class,
      FacesComponent.class,
      FacesBehavior.class,
      FacesConverter.class,
      NamedEvent.class,
      FacesRenderer.class,
      FacesBehaviorRenderer.class,
      FacesValidator.class);

  private final static Map<Class<? extends Annotation>, Set<Class<?>>> typeMap = new HashMap<>();

  public static void init(AnnotationDatabase annotationDatabase) {
    for (Class<? extends Annotation> clazz : TYPES) {
      typeMap.put(clazz, annotationDatabase.getTypes(clazz));
    }
  }

  @Override
  public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(ExternalContext ctx) {
    return typeMap;
  }

  @Override
  public Set<URL> getBaseUrls() throws IOException {
    return Collections.emptySet();
  }

}
