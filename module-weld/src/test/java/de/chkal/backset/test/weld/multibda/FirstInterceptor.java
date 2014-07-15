package de.chkal.backset.test.weld.multibda;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@First
@Interceptor
public class FirstInterceptor {

  @AroundInvoke
  public Object aroundInvoke(InvocationContext ic) throws Exception {
    return "FirstInterceptor[" + ic.proceed().toString() + "]";
  }

}
