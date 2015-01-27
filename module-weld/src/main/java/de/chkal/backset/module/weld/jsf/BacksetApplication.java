package de.chkal.backset.module.weld.jsf;

import org.jboss.weld.el.WeldELContextListener;

import javax.el.ExpressionFactory;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.faces.application.Application;
import javax.faces.application.ApplicationWrapper;

public class BacksetApplication extends ApplicationWrapper {

  private final Application wrapped;

  private final BeanManager beanManager;

  private ExpressionFactory expressionFactory;

  public BacksetApplication(Application wrapped) {

    this.wrapped = wrapped;

    beanManager = CDI.current().getBeanManager();

    super.addELResolver(beanManager.getELResolver());
    super.addELContextListener(new WeldELContextListener());

  }

  @Override
  public Application getWrapped() {
    return wrapped;
  }

  @Override
  public ExpressionFactory getExpressionFactory() {
    if (expressionFactory == null) {
      expressionFactory = beanManager.wrapExpressionFactory(wrapped.getExpressionFactory());
    }
    return expressionFactory;
  }

}
