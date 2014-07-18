package de.chkal.backset.module.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class JNDIModule implements Module {

  @Override
  public void init(ModuleContext context) {

    System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
    System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

    try {

      InitialContext initialContext = new InitialContext();

      initialContext.createSubcontext("java:");
      initialContext.createSubcontext("java:/comp");
      initialContext.createSubcontext("java:/comp/env");

    } catch (NamingException e) {
      throw new IllegalStateException("Failed to setup JNDI environment", e);
    }

  }

  @Override
  public void destroy() {
  }

}
