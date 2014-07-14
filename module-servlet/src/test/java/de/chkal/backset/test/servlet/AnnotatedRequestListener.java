package de.chkal.backset.test.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AnnotatedRequestListener implements ServletRequestListener {

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    sre.getServletRequest().setAttribute(AnnotatedRequestListener.class.getName(),
        "AnnotatedRequestListener is active");
  }

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
  }

}
