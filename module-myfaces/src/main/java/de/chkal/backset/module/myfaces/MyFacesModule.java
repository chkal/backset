package de.chkal.backset.module.myfaces;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class MyFacesModule implements Module {

  @Override
  public void init(ModuleContext context) {
    context.register(new MyFacesDeploymentEnricher());
  }

  @Override
  public void destroy() {
  }

}
