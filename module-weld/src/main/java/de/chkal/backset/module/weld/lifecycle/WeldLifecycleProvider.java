package de.chkal.backset.module.weld.lifecycle;

import de.chkal.backset.module.api.LifecycleProvider;
import de.chkal.backset.module.weld.WeldBootstrapInitializer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WeldLifecycleProvider implements LifecycleProvider {

  @Override
  public int getPriority() {
    return 100;
  }

  @Override
  public void init(Object obj) {
    invokeLifecycleMethod(obj, PostConstruct.class);
  }

  @Override
  public void destroy(Object obj) {
    invokeLifecycleMethod(obj, PreDestroy.class);
  }

  private void invokeLifecycleMethod(Object obj, Class<? extends Annotation> annotationType) {

    if (WeldBootstrapInitializer.isInitialized()) {

      for (Method method : obj.getClass().getMethods()) {

        if (method.getAnnotation(annotationType) != null) {

          try {

            method.invoke(obj);

          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
          }

        }

      }
    }

  }

}
