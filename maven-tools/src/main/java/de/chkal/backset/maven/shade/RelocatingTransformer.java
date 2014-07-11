package de.chkal.backset.maven.shade;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.codehaus.plexus.util.IOUtil;

public class RelocatingTransformer implements ResourceTransformer {

  private final Logger log = new ConsoleLogger(Logger.LEVEL_INFO,
      RelocatingTransformer.class.getName());

  protected List<String> files = new ArrayList<>();

  private Map<String, InMemoryFile> output = new HashMap<>();

  @Override
  public boolean canTransformResource(String resource) {
    for (String file : files) {
      if (resource.equals(file)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void processResource(String resource, InputStream is, List<Relocator> relocators)
      throws IOException {

    for (int i = 1; i < 1000; i++) {

      String newResource = resource + "." + i;

      if (!output.containsKey(newResource)) {

        log.info("Relocation " + resource + " to: " + newResource);
        output.put(newResource, new InMemoryFile(is));
        return;
      }

    }

    throw new IllegalArgumentException("Cannot find new name for: " + resource);

  }

  @Override
  public boolean hasTransformedResource() {
    return output.size() > 0;
  }

  @Override
  public void modifyOutputStream(JarOutputStream os) throws IOException {

    for (Entry<String, InMemoryFile> entry : output.entrySet()) {
      os.putNextEntry(new ZipEntry(entry.getKey()));
      entry.getValue().writeTo(os);
    }

  }

  private static class InMemoryFile {

    private final byte[] bytes;

    public InMemoryFile(InputStream in) throws IOException {
      bytes = IOUtil.toByteArray(in);
    }

    public void writeTo(OutputStream out) throws IOException {
      IOUtil.copy(bytes, out);
    }

  }

}
