package de.chkal.backset.module.weld;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.ejb.spi.EjbDescriptor;
import org.jboss.weld.xml.BeansXmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BacksetDeploymentArchive implements BeanDeploymentArchive {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final SimpleServiceRegistry serviceRegistry;
  private final BeansXml beansXml;
  private final Set<String> beanClasses;

  public BacksetDeploymentArchive(Set<String> beanClasses) {

    this.beanClasses = beanClasses;

    serviceRegistry = new SimpleServiceRegistry();

    beansXml = new BeansXmlParser().parse(getBeansXmlFiles());

  }

  private List<URL> getBeansXmlFiles() {

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

  @Override
  public String getId() {
    return "backset";
  }

  @Override
  public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
    return Collections.emptyList();
  }

  @Override
  public Collection<String> getBeanClasses() {
    return beanClasses;
  }

  @Override
  public BeansXml getBeansXml() {
    return beansXml;
  }

  @Override
  public Collection<EjbDescriptor<?>> getEjbs() {
    return Collections.emptyList();
  }

  @Override
  public ServiceRegistry getServices() {
    return serviceRegistry;
  }

}
