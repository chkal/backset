package de.chkal.backset.module.jsp.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.apache.jasper.deploy.TagLibraryInfo;
import org.junit.Test;

import de.chkal.backset.module.jsp.xml.TaglibParser;

public class StandardTaglibsTest {

  private final TaglibParser parser = new TaglibParser();

  @Test
  public void canParseCoreTaglib() {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/c.tld");
    assertThat(stream).isNotNull();

    TagLibraryInfo tagLibInfo = parser.parse(stream);
    assertThat(tagLibInfo).isNotNull();

  }

  @Test
  public void canParseSqlTaglib() {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/sql.tld");
    assertThat(stream).isNotNull();

    TagLibraryInfo tagLibInfo = parser.parse(stream);
    assertThat(tagLibInfo).isNotNull();

  }

  @Test
  public void canParseFmtTaglib() {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/fmt.tld");
    assertThat(stream).isNotNull();

    TagLibraryInfo tagLibInfo = parser.parse(stream);
    assertThat(tagLibInfo).isNotNull();

  }

  @Test
  public void canParseXTaglib() {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/x.tld");
    assertThat(stream).isNotNull();

    TagLibraryInfo tagLibInfo = parser.parse(stream);
    assertThat(tagLibInfo).isNotNull();

  }

}
