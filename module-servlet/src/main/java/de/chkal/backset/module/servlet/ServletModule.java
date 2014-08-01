package de.chkal.backset.module.servlet;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.servlet.annotation.WebFilterDeploymentEnricher;
import de.chkal.backset.module.servlet.annotation.WebListenerDeploymentEnricher;
import de.chkal.backset.module.servlet.annotation.WebServletDeploymentEnricher;
import de.chkal.backset.module.servlet.xml.DescriptorDeploymentEnricher;

public class ServletModule implements Module {

  @Override
  public int getPriority() {
    return 1000;
  }

  @Override
  public void init(ModuleContext moduleContext) {

    ServletEnricherContext enricherContext = new ServletEnricherContext();

    moduleContext.register(new DescriptorDeploymentEnricher(enricherContext));

    moduleContext.register(new WebServletDeploymentEnricher(moduleContext, enricherContext));
    moduleContext.register(new WebFilterDeploymentEnricher(moduleContext, enricherContext));
    moduleContext.register(new WebListenerDeploymentEnricher(moduleContext, enricherContext));

  }

  @Override
  public void destroy() {
  }

}
