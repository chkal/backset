package de.chkal.backset.module.weld;

import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.BeansXml;
import org.jboss.weld.ejb.spi.EjbDescriptor;
import org.jboss.weld.xml.BeansXmlParser;

public class BacksetDeploymentArchive implements BeanDeploymentArchive {

  private final SimpleServiceRegistry serviceRegistry;
  private final BeansXml beansXml;
  private final Set<String> beanClasses;

  public BacksetDeploymentArchive(Set<String> beanClasses) {

    this.beanClasses = beanClasses;

    serviceRegistry = new SimpleServiceRegistry();

    BeansXmlParser beansXmlParser = new BeansXmlParser();
    beansXml = beansXmlParser.parse(Collections.<URL> emptyList());

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
