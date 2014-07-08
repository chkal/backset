package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.List;

import de.chkal.backset.core.config.Section;

@Section("undertow")
public class UndertowConfig {

  private int ioThreads;

  private int workerThreads;

  private List<UndertowConnectorConfig> connectors = new ArrayList<>();

  public int getIoThreads() {
    return ioThreads;
  }

  public void setIoThreads(int ioThreads) {
    this.ioThreads = ioThreads;
  }

  public int getWorkerThreads() {
    return workerThreads;
  }

  public void setWorkerThreads(int workerThreads) {
    this.workerThreads = workerThreads;
  }

  public List<UndertowConnectorConfig> getConnectors() {
    return connectors;
  }

  public void setConnectors(List<UndertowConnectorConfig> connectors) {
    this.connectors = connectors;
  }

}
