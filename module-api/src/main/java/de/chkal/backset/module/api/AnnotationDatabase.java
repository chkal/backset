package de.chkal.backset.module.api;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface AnnotationDatabase {

  Set<Class<?>> getTypes(Class<? extends Annotation> annotation);

  Set<String> getTypeNamesByPackage(String packageName);

}
