package de.chkal.backset.module.test.merge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.shade.DefaultShader;
import org.apache.maven.plugins.shade.ShadeRequest;
import org.apache.maven.plugins.shade.filter.Filter;
import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.apache.maven.plugins.shade.resource.ServicesResourceTransformer;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class ShadePluginArchiveMerger {

  private JavaArchive base;

  private final List<Archive<?>> archives = new ArrayList<>();

  private final List<File> tempFiles = new ArrayList<>();

  private ShadePluginArchiveMerger(JavaArchive base) {
    this.base = base;
  }

  public static ShadePluginArchiveMerger create(JavaArchive base) {
    return new ShadePluginArchiveMerger(base);
  }

  public ShadePluginArchiveMerger merge(Iterable<Archive<?>> archives) {
    for (Archive<?> archive : archives) {
      merge(archive);
    }
    return this;
  }

  public ShadePluginArchiveMerger merge(Archive<?>[] archives) {
    for (Archive<?> archive : archives) {
      merge(archive);
    }
    return this;
  }

  public ShadePluginArchiveMerger merge(Archive<?> other) {
    archives.add(other);
    return this;
  }

  public JavaArchive getResult() {

    try {

      Set<File> files = new LinkedHashSet<>();

      files.add(asFile(base));

      for (Archive<?> archive : archives) {
        files.add(asFile(archive));
      }

      File uberJar = createTempFile();

      ShadeRequest request = new ShadeRequest();
      request.setUberJar(uberJar);
      request.setJars(files);
      request.setFilters(new ArrayList<Filter>());
      request.setRelocators(new ArrayList<Relocator>());
      request.setShadeSourcesContent(false);
      request.setResourceTransformers(getDefaultResourceTransformers());

      DefaultShader shader = new DefaultShader();
      shader.enableLogging(new ConsoleLogger());
      shader.shade(request);

      return ShrinkWrap.create(ZipImporter.class, base.getName())
          .importFrom(uberJar)
          .as(JavaArchive.class);

    } catch (MojoExecutionException | IOException e) {
      throw new IllegalStateException("Failed", e);
    } finally {
      deleteTempFiles();
    }

  }

  private void deleteTempFiles() {
    for (File file : tempFiles) {
      file.delete();
    }
  }

  private List<ResourceTransformer> getDefaultResourceTransformers() {

    ResourceTransformer servicesTransformer = new ServicesResourceTransformer();

    return Arrays.asList(servicesTransformer);

  }

  private File asFile(Archive<?> archive) {
    File file = createTempFile();
    archive.as(ZipExporter.class).exportTo(file);
    return file;
  }

  private File createTempFile() {
    String tmpDir = System.getProperty("java.io.tmpdir");
    String jarFile = UUID.randomUUID().toString() + ".jar";
    File file = Paths.get(tmpDir, jarFile).toFile();
    tempFiles.add(file);
    return file;
  }

}
