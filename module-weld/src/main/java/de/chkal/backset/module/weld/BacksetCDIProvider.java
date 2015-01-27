package de.chkal.backset.module.weld;

import org.jboss.weld.SimpleCDI;
import org.jboss.weld.manager.BeanManagerImpl;

import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.CDIProvider;

public class BacksetCDIProvider implements CDIProvider {

  private static class EnhancedCDI extends SimpleCDI {

    @Override
    protected BeanManagerImpl unsatisfiedBeanManager(String callerClassName) {
      if (getContainer().beanDeploymentArchives().values().size() == 1) {
        return getContainer().beanDeploymentArchives().values().iterator().next();
      }
      return super.unsatisfiedBeanManager(callerClassName);
    }
  }

  @Override
  public CDI<Object> getCDI() {
    return new EnhancedCDI();
  }

}
