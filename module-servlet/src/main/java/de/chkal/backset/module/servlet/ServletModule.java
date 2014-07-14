package de.chkal.backset.module.servlet;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class ServletModule implements Module {

  @Override
  public void init(ModuleContext context) {

    context.register(new WebServletDeploymentEnricher(context));
    context.register(new WebFilterDeploymentEnricher(context));
    context.register(new WebListenerDeploymentEnricher(context));

  }

  @Override
  public void destroy() {
  }

}
