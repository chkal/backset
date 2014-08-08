package de.chkal.backset.module.jsp.config;

import java.util.List;

import de.chkal.backset.module.api.Section;

@Section("jsp")
public class JSPConfig {

  private List<String> tldFiles;

  public List<String> getTldFiles() {
    return tldFiles;
  }

  public void setTldFiles(List<String> tldFiles) {
    this.tldFiles = tldFiles;
  }

}
