package de.chkal.backset.module.test.merge;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class SimpleArchiveMerger {

  private JavaArchive base;

  private SimpleArchiveMerger(JavaArchive base) {
    this.base = base;
  }

  public static SimpleArchiveMerger create(JavaArchive base) {
    return new SimpleArchiveMerger(base);
  }

  public SimpleArchiveMerger merge(Iterable<Archive<?>> archives) {
    for (Archive<?> archive : archives) {
      merge(archive);
    }
    return this;
  }

  public SimpleArchiveMerger merge(Archive<?>[] archives) {
    for (Archive<?> archive : archives) {
      merge(archive);
    }
    return this;
  }

  public SimpleArchiveMerger merge(Archive<?> other) {

    Set<ArchivePath> paths = new HashSet<>();
    paths.addAll(base.getContent().keySet());
    paths.addAll(other.getContent().keySet());

    JavaArchive merged = ShrinkWrap.create(JavaArchive.class);

    for (ArchivePath path : paths) {

      Node existingNode = base.get(path);
      Node newNode = other.get(path);

      if (existingNode != null && newNode != null) {

        // directories
        if (existingNode.getAsset() == null && newNode.getAsset() == null) {
          merged.addAsDirectory(path);
        }

        // files
        else if (existingNode.getAsset() != null && newNode.getAsset() != null) {
          merged.add(merge(path, existingNode.getAsset(), newNode.getAsset()), path);
        }

        else {
          throw new IllegalStateException("Mixed file/directory situation");
        }

      }

      else {

        Node node = existingNode != null ? existingNode : newNode;

        if (node.getAsset() == null) {
          merged.addAsDirectories(path);
        } else {
          merged.add(node.getAsset(), path);
        }

      }

    }

    this.base = merged;
    return this;

  }

  public JavaArchive getResult() {
    return base;
  }

  private Asset merge(ArchivePath path, Asset existing, Asset added) {
    if (path.get().startsWith("/META-INF/services/")) {
      return mergeServiceProviders(existing, added);
    }
    return added;
  }

  private Asset mergeServiceProviders(Asset existing, Asset added) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      IOUtils.copy(existing.openStream(), bos);
      IOUtils.write("\n", bos);
      IOUtils.copy(added.openStream(), bos);
      return new ByteArrayAsset(bos.toByteArray());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}
