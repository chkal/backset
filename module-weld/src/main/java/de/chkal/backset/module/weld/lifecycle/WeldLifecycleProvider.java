package de.chkal.backset.module.weld.lifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import de.chkal.backset.module.api.LifecycleProvider;

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

    for (Method method : obj.getClass().getMethods()) {

      if (method.getAnnotation(annotationType) != null) {

        try {

          method.invoke(obj);

        } catch (IllegalAccessException e) {
          throw new IllegalStateException(e);
        } catch (IllegalArgumentException e) {
          throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
          throw new IllegalStateException(e);
        }

      }

    }

  }

}
