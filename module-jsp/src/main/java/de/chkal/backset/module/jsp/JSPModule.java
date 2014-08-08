package de.chkal.backset.module.jsp;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class JSPModule implements Module {

  @Override
  public int getPriority() {
    return 5;
  }

  @Override
  public void init(ModuleContext context) {

    JSPDeploymentEnricher.configManager = context.getConfigManager();
    
    context.register(new JSPDeploymentEnricher());

  }

  @Override
  public void destroy() {
  }

}
