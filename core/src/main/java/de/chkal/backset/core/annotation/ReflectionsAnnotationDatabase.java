package de.chkal.backset.core.annotation;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.AnnotationDatabase;

public class ReflectionsAnnotationDatabase implements AnnotationDatabase {

  private final Logger log = LoggerFactory.getLogger(ReflectionsAnnotationDatabase.class);

  private final Reflections reflections;

  public ReflectionsAnnotationDatabase() {

    long start = System.currentTimeMillis();
    log.info("Scanning for annotations...");

    reflections = new ConfigurationBuilder()
        .addUrls(ClasspathHelper.forClassLoader())
        .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner(false))
        .build();

    long duration = System.currentTimeMillis() - start;
    log.info("Annotation scanning took {}ms", duration);

  }

  @Override
  public Set<Class<?>> getTypes(Class<? extends Annotation> annotationType) {
    return reflections.getTypesAnnotatedWith(annotationType);
  }

  @Override
  public Set<String> getTypeNamesByPackage(String packageName) {

    Set<String> result = new HashSet<>();

    Set<String> xxx = reflections.getStore().getSubTypesOf(Object.class.getName());
    System.out.println("--- total classes: " + xxx.size());
    for (String type : xxx) {

      if (type.startsWith(packageName)) {
        result.add(type);
      }

    }
    return result;

  }

}
