package de.chkal.backset.module.myfaces.annotation;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class SimpleFacesBean {

  private String value = "Not initialized";

  @PostConstruct
  public void init() {
    value = "Hey! I'm a managed bean!";
  }

  public String getValue() {
    return value;
  }

}
