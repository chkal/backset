package de.chkal.backset.module.jsf.mojarra;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import javax.faces.webapp.FacesServlet;

public class JsfMojarraDeploymentEnricher implements DeploymentEnricher {

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {
        deployment
                .addInitParameter("javax.faces.PROJECT_STAGE", "Development")
                .addInitParameter("com.sun.faces.forceLoadConfiguration", "true")
                .addListener(Servlets.listener(com.sun.faces.config.ConfigureListener.class))
                .addServlets(Servlets.servlet("FacesServlet", FacesServlet.class).addMapping("*.jsf"));
    }

}
