package de.chkal.backset.module.myfaces;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.faces.context.ExternalContext;

import org.apache.myfaces.config.DefaultFacesConfigResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BacksetFacesConfigResourceProvider extends DefaultFacesConfigResourceProvider {

  private final Logger log = LoggerFactory.getLogger(BacksetFacesConfigResourceProvider.class);

  @Override
  public Collection<URL> getMetaInfConfigurationResources(ExternalContext context)
      throws IOException {

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

    Set<URL> result = new HashSet<>();
    result.addAll(super.getMetaInfConfigurationResources(context));

    for (int i = 1; i < 1000; i++) {

      String relocatedResourceName = "META-INF/faces-config.xml." + i;

      URL resource = classLoader.getResource(relocatedResourceName);

      if (resource != null) {
        log.info("Found relocated faces-config.xml file: " + relocatedResourceName);
        result.add(resource);
      } else {
        break;
      }

    }

    return result;

  }

}
