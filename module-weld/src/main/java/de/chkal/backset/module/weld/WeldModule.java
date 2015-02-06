package de.chkal.backset.module.weld;

import de.chkal.backset.module.api.Module;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.weld.lifecycle.WeldInjectionProvider;
import de.chkal.backset.module.weld.lifecycle.WeldLifecycleProvider;

public class WeldModule implements Module {

  @Override
  public int getPriority() {
    return 100;
  }

  @Override
  public void init(ModuleContext context) {

    WeldBootstrapInitializer.annotationDatabase = context.getAnnotationDatabase();
    WeldBootstrapInitializer.weldConfig =
        context.getConfigManager().getConfig(WeldConfig.class);

    context.register(new WeldDeploymentEnricher());

    context.register(new WeldLifecycleProvider());
    context.register(new WeldInjectionProvider());

  }

  @Override
  public void destroy() {
  }

}
