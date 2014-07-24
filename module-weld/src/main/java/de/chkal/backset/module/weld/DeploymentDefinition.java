package de.chkal.backset.module.weld;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.weld.support.BacksetProducer;

public class DeploymentDefinition {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final WeldConfig weldConfig;

  private final AnnotationDatabase annotationDatabase;

  public DeploymentDefinition(WeldConfig weldConfig, AnnotationDatabase annotationDatabase) {
    this.weldConfig = weldConfig;
    this.annotationDatabase = annotationDatabase;
  }

  public Set<String> getBeanClasses() {

    Set<String> beanClasses = new HashSet<>();

    if (weldConfig != null && weldConfig.getPackages() != null) {
      for (String packageName : weldConfig.getPackages()) {
        Set<String> types = annotationDatabase.getTypeNamesByPackage(packageName);
        log.info("Found {} types in package: {}", types.size(), packageName);
        beanClasses.addAll(types);
      }
    }

    if (beanClasses.isEmpty()) {
      log.warn("No bean classes found! "
          + "You should tell the Weld module which packages contain your CDI beans");
    }

    // add some custom backset classes here
    beanClasses.add(BacksetProducer.class.getName());

    return beanClasses;

  }

  public List<URL> getBeansXmlFiles() {

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    List<URL> result = new ArrayList<>();

    URL webInfBeansXml = classLoader.getResource("webapp/WEB-INF/beans.xml");
    if (webInfBeansXml != null) {
      log.debug("Found default beans.xml: {}", webInfBeansXml.getPath());
      result.add(webInfBeansXml);
    }

    URL metaInfBeansXml = classLoader.getResource("META-INF/beans.xml");
    if (metaInfBeansXml != null) {
      log.debug("Found default beans.xml: {}", metaInfBeansXml.getPath());
      result.add(metaInfBeansXml);
    }

    for (int i = 1; i < 1000; i++) {

      String relocatedResourceName = "META-INF/beans.xml." + i;

      URL resource = classLoader.getResource(relocatedResourceName);

      if (resource != null) {
        log.debug("Found relocated beans.xml file: {}", resource.getPath());
        result.add(resource);
      } else {
        break;
      }

    }

    return result;
  }

}
