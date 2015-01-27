package de.chkal.backset.module.weld.jsf;

import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;

public class BacksetApplicationFactory extends ApplicationFactory {

  private final ApplicationFactory delegate;

  private Application application;

  public BacksetApplicationFactory(ApplicationFactory delegate) {
    this.delegate = delegate;
  }

  @Override
  public Application getApplication() {
    if (application == null) {
      this.application = new BacksetApplication(delegate.getApplication());
    }
    return application;
  }

  @Override
  public void setApplication(Application application) {
    this.application = null;
    delegate.setApplication(application);
  }

}
