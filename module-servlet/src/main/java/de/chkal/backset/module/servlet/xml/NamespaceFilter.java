package de.chkal.backset.module.servlet.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

class NamespaceFilter extends XMLFilterImpl {

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    super.startElement(normalizeNamespace(uri), localName, qName, atts);
  }

  private String normalizeNamespace(String url) {
    if (url != null) {
      return url
          .replace("http://java.sun.com/xml/ns/javaee", "http://xmlns.jcp.org/xml/ns/javaee")
          .intern();
    }
    return null;
  }

}
