package de.chkal.backset.module.weld.support;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import de.chkal.backset.core.Singletons;
import de.chkal.backset.module.api.ConfigManager;

public class BacksetProducer {

  @Produces
  @ApplicationScoped
  public ConfigManager produceConfigManager() {
    return Singletons.get(ConfigManager.class);
  }

}
