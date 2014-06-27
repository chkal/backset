package de.chkal.backset.module.weld.basic;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class SimpleCDIModule implements Module {

  @Override
  public void init(ModuleContext context) {
    context.register(new SimpleCDIDeploymentEnricher());
  }

  @Override
  public void destroy() {
  }

}
