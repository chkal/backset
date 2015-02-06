package de.chkal.backset.module.weld.lifecycle;

import de.chkal.backset.module.api.InjectionProvider;
import de.chkal.backset.module.weld.WeldBootstrapInitializer;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;

public class WeldInjectionProvider implements InjectionProvider {

  @Override
  public int getPriority() {
    return 100;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public void inject(Object o) {

    if (WeldBootstrapInitializer.isInitialized()) {

      BeanManager beanManager = CDI.current().getBeanManager();

      CreationalContext creationalContext = beanManager.createCreationalContext(null);
      AnnotatedType annotatedType = beanManager.createAnnotatedType(o.getClass());
      InjectionTarget injectionTarget = beanManager.createInjectionTarget(annotatedType);

      injectionTarget.inject(o, creationalContext);

    }

  }

}
