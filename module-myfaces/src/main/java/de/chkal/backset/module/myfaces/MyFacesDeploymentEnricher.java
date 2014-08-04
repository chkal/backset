package de.chkal.backset.module.myfaces;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;

import javax.faces.webapp.FacesServlet;

import org.apache.myfaces.webapp.StartupServletContextListener;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;

public class MyFacesDeploymentEnricher implements DeploymentEnricher {

  @Override
  public int getPriority() {
    return 200;
  }
  
  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {
    deployment
        .addInitParameter(
            "org.apache.myfaces.INITIALIZE_ALWAYS_STANDALONE", "true")
        .addInitParameter(
            "org.apache.myfaces.EXPRESSION_FACTORY", "de.odysseus.el.ExpressionFactoryImpl")
        .addListener(
            Servlets.listener(StartupServletContextListener.class))
        .addServlets(
            Servlets.servlet("FacesServlet", FacesServlet.class).addMapping("*.jsf"));
  }

}
