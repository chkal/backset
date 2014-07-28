package de.chkal.backset.test.weld.support.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.chkal.backset.module.api.ConfigManager;

@ApplicationScoped
public class ConfigSupportBean {

  @Inject
  private ConfigManager configManager;

  public ConfigManager getConfigManager() {
    return configManager;
  }

  public void setConfigManager(ConfigManager configManager) {
    this.configManager = configManager;
  }

}
