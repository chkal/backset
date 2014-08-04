package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.chkal.backset.core.lifecycle.LifecycleContext;
import de.chkal.backset.module.api.AnnotationDatabase;
import de.chkal.backset.module.api.ConfigManager;
import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.api.InjectionProvider;
import de.chkal.backset.module.api.LifecycleProvider;
import de.chkal.backset.module.api.ModuleContext;
import de.chkal.backset.module.api.OrderedComparator;

public class DefaultModuleContext implements ModuleContext, LifecycleContext {

  private final List<DeploymentEnricher> deploymentEnrichers = new ArrayList<>();
  private final List<InjectionProvider> injectionProviders = new ArrayList<>();
  private final List<LifecycleProvider> lifecycleProviders = new ArrayList<>();
  private final AnnotationDatabase annotationDatabase;
  private final ConfigManager configManager;

  public DefaultModuleContext(AnnotationDatabase annotationDatabase, ConfigManager configManager) {
    this.annotationDatabase = annotationDatabase;
    this.configManager = configManager;
  }

  @Override
  public void register(DeploymentEnricher deploymentEnricher) {
    this.deploymentEnrichers.add(deploymentEnricher);
    Collections.sort(this.deploymentEnrichers, OrderedComparator.INSTANCE);
  }

  @Override
  public void register(InjectionProvider injectionProvider) {
    this.injectionProviders.add(injectionProvider);
    Collections.sort(this.injectionProviders, OrderedComparator.INSTANCE);
  }

  @Override
  public void register(LifecycleProvider lifecycleProvider) {
    this.lifecycleProviders.add(lifecycleProvider);
    Collections.sort(this.lifecycleProviders, OrderedComparator.INSTANCE);
  }

  public List<DeploymentEnricher> getDeploymentEnrichers() {
    return Collections.unmodifiableList(deploymentEnrichers);
  }

  @Override
  public AnnotationDatabase getAnnotationDatabase() {
    return annotationDatabase;
  }

  @Override
  public ConfigManager getConfigManager() {
    return configManager;
  }

  @Override
  public Iterable<InjectionProvider> getInjectionProviders() {
    return injectionProviders;
  }

  @Override
  public Iterable<LifecycleProvider> getLifecycleProviders() {
    return lifecycleProviders;
  }

}
