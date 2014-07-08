package de.chkal.backset.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.chkal.backset.module.api.ConfigManager;

public class DefaultConfigManager implements ConfigManager {

  private final ObjectMapper mapper;

  private final List<JsonNode> configTrees = new ArrayList<>();

  public DefaultConfigManager() {
    mapper = new ObjectMapper(new YAMLFactory());
  }

  protected ConfigManager addConfig(Reader reader) throws IOException {
    configTrees.add(mapper.readTree(reader));
    return this;
  }

  protected ConfigManager addConfig(InputStream stream) throws IOException {
    configTrees.add(mapper.readTree(stream));
    return this;
  }

  public <T> T getConfig(Class<T> type) {

    String section = getSectionName(type);

    try {

      for (JsonNode configTree : configTrees) {

        JsonNode subtree = configTree.get(section);
        if (subtree != null) {
          return mapper.readValue(mapper.writeValueAsString(subtree), type);
        }

      }
      return null;

    } catch (IOException e) {
      throw new IllegalStateException(e);
    }

  }

  private String getSectionName(Class<?> type) {

    Section annotation = type.getAnnotation(Section.class);
    if (annotation != null) {
      return annotation.value().trim();
    }
    throw new IllegalStateException("There is no @Section annotation on: " + type.getName());

  }

}
