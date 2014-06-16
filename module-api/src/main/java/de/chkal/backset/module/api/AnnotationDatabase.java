package de.chkal.backset.module.api;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface AnnotationDatabase {

  Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationType);

}
