package de.chkal.backset.core.config;

import de.chkal.backset.module.api.Section;

@Section("foobar")
public class FoobarConfig {
  
  private String item;

  private int count;
  
  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
