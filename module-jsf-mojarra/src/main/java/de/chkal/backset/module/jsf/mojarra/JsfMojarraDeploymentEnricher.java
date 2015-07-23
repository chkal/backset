package de.chkal.backset.module.jsf.mojarra;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InstanceFactoryFactory;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import javax.faces.webapp.FacesServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsfMojarraDeploymentEnricher implements DeploymentEnricher {

    private static final Logger log = LoggerFactory.getLogger(JsfMojarraModule.class);

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public void enrich(DeploymentInfo deployment, InstanceFactoryFactory factory) {
        deployment
                .addInitParameter("javax.faces.PROJECT_STAGE", "Development")
                .addInitParameter("com.sun.faces.forceLoadConfiguration", "true")
                //      .addInitParameter("javax.faces.CONFIG_FILES", "/WEB-INF")
                // .addInitParameter("com.sun.faces.injectionProvider", "com.sun.faces.vendor.WebContainerInjectionProvider")
                .addListener(Servlets.listener(com.sun.faces.config.ConfigureListener.class))
                .addServlets(Servlets.servlet("FacesServlet", FacesServlet.class).addMapping("*.jsf"));
    }

}
