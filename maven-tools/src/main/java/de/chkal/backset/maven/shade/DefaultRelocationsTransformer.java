package de.chkal.backset.maven.shade;

public class DefaultRelocationsTransformer extends RelocatingTransformer {

  public DefaultRelocationsTransformer() {
    files.add("META-INF/faces-config.xml");
    files.add("META-INF/beans.xml");
    files.add("META-INF/web-fragment.xml");
  }

}
