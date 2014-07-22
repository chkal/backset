package de.chkal.backset.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chkal.backset.module.api.Section;

@Section("undertow")
public class UndertowConfig {

  private int ioThreads;

  private int workerThreads;

  private List<UndertowConnectorConfig> connectors = new ArrayList<>();

  private Map<String, String> contextParams = new HashMap<>();

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

  public Map<String, String> getContextParams() {
    return contextParams;
  }

  public void setContextParams(Map<String, String> contextParams) {
    this.contextParams = contextParams;
  }

}
