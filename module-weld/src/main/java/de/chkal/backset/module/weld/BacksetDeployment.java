package de.chkal.backset.module.weld;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.inject.spi.Extension;

import org.jboss.weld.bootstrap.api.Bootstrap;
import org.jboss.weld.bootstrap.api.ServiceRegistry;
import org.jboss.weld.bootstrap.api.helpers.SimpleServiceRegistry;
import org.jboss.weld.bootstrap.spi.BeanDeploymentArchive;
import org.jboss.weld.bootstrap.spi.CDI11Deployment;
import org.jboss.weld.bootstrap.spi.Metadata;

public class BacksetDeployment implements CDI11Deployment {

  private final SimpleServiceRegistry serviceRegistry;
  private final BeanDeploymentArchive deploymentArchive;
  private final Iterable<Metadata<Extension>> extensions;

  public BacksetDeployment(Bootstrap bootstrap, BacksetDeploymentArchive deploymentArchive) {
    this.deploymentArchive = deploymentArchive;
    this.serviceRegistry = new SimpleServiceRegistry();
    this.extensions = bootstrap.loadExtensions(Thread.currentThread().getContextClassLoader());
  }

  @Override
  public Collection<BeanDeploymentArchive> getBeanDeploymentArchives() {
    return Arrays.asList(deploymentArchive);
  }

  @Override
  public BeanDeploymentArchive loadBeanDeploymentArchive(Class<?> beanClass) {
    return deploymentArchive;
  }

  @Override
  public ServiceRegistry getServices() {
    return serviceRegistry;
  }

  @Override
  public Iterable<Metadata<Extension>> getExtensions() {
    return extensions;
  }

  @Override
  public BeanDeploymentArchive getBeanDeploymentArchive(Class<?> beanClass) {
    return deploymentArchive;
  }

}
