package de.chkal.backset.module.servlet;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.servlet.xml.DescriptorDeploymentEnricher;

public class ServletModule implements Module {

  @Override
  public int getPriority() {
    return 1000;
  }
  
  @Override
  public void init(ModuleContext context) {

    context.register(new DescriptorDeploymentEnricher());
    context.register(new WebServletDeploymentEnricher(context));
    context.register(new WebFilterDeploymentEnricher(context));
    context.register(new WebListenerDeploymentEnricher(context));

  }

  @Override
  public void destroy() {
  }

}
