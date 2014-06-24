package de.chkal.backset.module.owb;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class OpenWebBeansModule implements Module {

  @Override
  public void init(ModuleContext context) {

    context.register(new OpenWebBeansDeploymentEnricher());

  }

  @Override
  public void destroy() {
  }

}
