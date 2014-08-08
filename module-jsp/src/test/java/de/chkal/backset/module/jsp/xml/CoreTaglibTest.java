package de.chkal.backset.module.jsp.xml;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.apache.jasper.deploy.TagAttributeInfo;
import org.apache.jasper.deploy.TagInfo;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.junit.Before;
import org.junit.Test;

public class CoreTaglibTest {

  private TagLibraryInfo tagLibInfo;

  @Before
  public void before() {

    InputStream stream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/c.tld");

    TaglibParser parser = new TaglibParser();
    tagLibInfo = parser.parse(stream);

  }

  @Test
  public void shouldParseTldWithoutErrors() {
    assertThat(tagLibInfo).isNotNull();
  }

  @Test
  public void shouldParseShortName() {
    assertThat(tagLibInfo.getShortname()).isEqualTo("c");
  }

  @Test
  public void shouldParsePrefix() {
    assertThat(tagLibInfo.getPrefix()).isEqualTo("c");
  }

  @Test
  public void shouldParseUri() {
    assertThat(tagLibInfo.getUri()).isEqualTo("http://java.sun.com/jsp/jstl/core");
  }

  @Test
  public void shouldParseTlibversion() {
    assertThat(tagLibInfo.getTlibversion()).isEqualTo("1.1");
  }

  @Test
  public void shouldParseJspVersion() {
    assertThat(tagLibInfo.getJspversion()).isEqualTo("2.1");
  }

  @Test
  public void shouldParseUrn() {
    assertThat(tagLibInfo.getUrn()).isEqualTo("http://java.sun.com/jsp/jstl/core");
  }

  @Test
  public void shouldParseValidator() {
    assertThat(tagLibInfo.getValidator()).isNotNull();
    assertThat(tagLibInfo.getValidator().getValidatorClass())
        .isEqualTo("org.apache.taglibs.standard.tlv.JstlCoreTLV");
  }

  @Test
  public void shouldParseAllTags() {
    assertThat(tagLibInfo.getTags()).hasSize(14);
  }

  @Test
  public void shouldParseCatchTag() {

    TagInfo tagInfo = tagLibInfo.getTags()[0];
    assertThat(tagInfo.getTagName()).isEqualTo("catch");
    assertThat(tagInfo.getTagClassName())
        .isEqualTo("org.apache.taglibs.standard.tag.common.core.CatchTag");
    assertThat(tagInfo.getBodyContent()).isEqualTo("JSP");

    TagAttributeInfo[] attributeInfos = tagInfo.getTagAttributeInfos();
    assertThat(attributeInfos).hasSize(1);

    TagAttributeInfo attributeInfo = attributeInfos[0];
    assertThat(attributeInfo.getName()).isEqualTo("var");
    assertThat(attributeInfo.getRequired()).isEqualTo("false");
    assertThat(attributeInfo.getReqTime()).isEqualTo("false");

  }
}
