package de.chkal.backset.module.weld;

import java.util.ArrayList;
import java.util.List;

public class WeldConfig {

  private List<String> packages = new ArrayList<>();

  public List<String> getPackages() {
    return packages;
  }

  public void setPackages(List<String> packages) {
    this.packages = packages;
  }

}
