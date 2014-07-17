package de.chkal.backset.module.bonecp.config;

import java.util.ArrayList;
import java.util.List;

import de.chkal.backset.module.api.Section;

@Section("bonecp")
public class BoneConfig {

  private List<BoneDatasource> datasources = new ArrayList<>();

  public List<BoneDatasource> getDatasources() {
    return datasources;
  }

  public void setDatasources(List<BoneDatasource> datasources) {
    this.datasources = datasources;
  }
  
}
