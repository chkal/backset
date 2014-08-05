package de.chkal.backset.module.jsp;

import io.undertow.jsp.HackInstanceManager;
import io.undertow.jsp.JspServletBuilder;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.util.HashMap;

import org.apache.jasper.deploy.JspPropertyGroup;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.apache.tomcat.InstanceManager;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;

public class JSPDeploymentEnricher implements DeploymentEnricher {

  @Override
  public int getPriority() {
    return 5;
  }

  @Override
  public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {
    
    HashMap<String, JspPropertyGroup> propertyGroups = new HashMap<String, JspPropertyGroup>();
    HashMap<String, TagLibraryInfo> tagLibraries = new HashMap<String, TagLibraryInfo>();
    InstanceManager instanceManager = new HackInstanceManager();
    JspServletBuilder.setupDeployment(deployment, propertyGroups, tagLibraries, instanceManager);
    
    ServletInfo jspServlet = JspServletBuilder.createServlet("JSPServlet", "*.jsp");
    
    deployment.addServlet(jspServlet);
    
  }

}
