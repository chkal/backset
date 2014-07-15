package de.chkal.backset.test.weld.multibda;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Second
@Interceptor
public class SecondInterceptor {

  @AroundInvoke
  public Object aroundInvoke(InvocationContext ic) throws Exception {
    return "SecondInterceptor[" + ic.proceed().toString() + "]";
  }

}
