package de.chkal.backset.module.test.merge;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ArchiveMergerTest {

  @Test
  public void mergeDifferentFilesIntoOneArchive() {

    JavaArchive first = ShrinkWrap.create(JavaArchive.class);
    first.add(new StringAsset("foo"), "first.txt");

    JavaArchive second = ShrinkWrap.create(JavaArchive.class);
    second.add(new StringAsset("bar"), "second.txt");

    JavaArchive result = ArchiveMerger.create(first).merge(second).getResult();

    assertThat(result.getContent()).hasSize(2);

    assertThat(getAsString(result, "first.txt")).isEqualTo("foo");
    assertThat(getAsString(result, "second.txt")).isEqualTo("bar");

  }

  @Test
  public void mergeByOverwriting() {

    JavaArchive first = ShrinkWrap.create(JavaArchive.class);
    first.add(new StringAsset("foo"), "data.txt");

    JavaArchive second = ShrinkWrap.create(JavaArchive.class);
    second.add(new StringAsset("bar"), "data.txt");

    JavaArchive result = ArchiveMerger.create(first).merge(second).getResult();

    assertThat(result.getContent()).hasSize(1);

    assertThat(getAsString(result, "data.txt"))
        .contains("bar")
        .doesNotContain("foo");

  }

  @Test
  public void mergeServiceProviderFiles() {

    JavaArchive first = ShrinkWrap.create(JavaArchive.class);
    first.add(new StringAsset("FirstImpl"), "META-INF/services/MyInterface");

    JavaArchive second = ShrinkWrap.create(JavaArchive.class);
    second.add(new StringAsset("SecondImpl"), "META-INF/services/MyInterface");

    JavaArchive result = ArchiveMerger.create(first).merge(second).getResult();

    assertThat(getAsString(result, "META-INF/services/MyInterface"))
        .contains("FirstImpl")
        .contains("SecondImpl")
        .isEqualTo("FirstImpl\nSecondImpl");

  }

  private String getAsString(Archive<?> archive, String path) {
    try {
      Node node = archive.get(path);
      if (node != null && node.getAsset() != null) {
        return IOUtils.toString(node.getAsset().openStream());
      }
      return null;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

}