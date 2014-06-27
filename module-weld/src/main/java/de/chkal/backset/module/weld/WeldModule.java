package de.chkal.backset.module.weld;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class WeldModule implements Module {

  @Override
  public void init(ModuleContext context) {

    System.out.println("====================");
    context.register(new WeldDeploymentEnricher());

  }

  @Override
  public void destroy() {
  }

}
