package de.chkal.backset.module.myfaces;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;

public class MyFacesModule implements Module {

  @Override
  public int getPriority() {
    return 300;
  }
  
  @Override
  public void init(ModuleContext context) {

    context.register(new MyFacesDeploymentEnricher());

    BacksetAnnotationProvider.init(context.getAnnotationDatabase());

  }

  @Override
  public void destroy() {
  }

}
