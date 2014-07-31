package de.chkal.backset.module.servlet.xml;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.servlet.api.ServletInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.List;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.servlet.xml.types.DispatcherType;
import de.chkal.backset.module.servlet.xml.types.FilterMappingType;
import de.chkal.backset.module.servlet.xml.types.FilterType;
import de.chkal.backset.module.servlet.xml.types.ListenerType;
import de.chkal.backset.module.servlet.xml.types.ParamValueType;
import de.chkal.backset.module.servlet.xml.types.ServletMappingType;
import de.chkal.backset.module.servlet.xml.types.ServletNameType;
import de.chkal.backset.module.servlet.xml.types.ServletType;
import de.chkal.backset.module.servlet.xml.types.UrlPatternType;
import de.chkal.backset.module.servlet.xml.types.WebAppType;
import de.chkal.backset.module.servlet.xml.types.WebFragmentType;

public class DescriptorDeploymentEnricher implements DeploymentEnricher {

  private final Logger log = LoggerFactory.getLogger(DescriptorDeploymentEnricher.class);

  private final JAXBContext context;

  private final ClassLoader classLoader;

  public DescriptorDeploymentEnricher() {

    classLoader = Thread.currentThread().getContextClassLoader();

    try {
      context = JAXBContext.newInstance(WebAppType.class.getPackage().getName());
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }

  }

  @Override
  public int getPriority() {
    return 1000;
  }
  
  @Override
  public void enrich(DeploymentInfo deployment) {

    /*
     * web.xml
     */
    InputStream webXmlStream = classLoader.getResourceAsStream("webapp/WEB-INF/web.xml");
    if (webXmlStream != null) {
      log.info("Processing standard web.xml file...");
      processWebXml(deployment, webXmlStream);
    }

    /*
     * unrelocated web-fragment.xml
     */
    try {
      Enumeration<URL> fragmentResources = classLoader.getResources("META-INF/web-fragment.xml");
      while (fragmentResources.hasMoreElements()) {
        URL fragment = fragmentResources.nextElement();
        log.info("Processing web-fragment.xml file: {}", fragment.toString());
        processWebFragmentXml(deployment, fragment.openStream());
      }
    } catch (IOException e) {
      log.warn("Failed to process web fragments", e);
    }

    /*
     * relocated web-fragment.xml
     */
    for (int i = 1; i < 1000; i++) {

      String resourceName = "META-INF/web-fragment.xml." + i;

      InputStream fragmentStream = classLoader.getResourceAsStream(resourceName.toString());

      if (fragmentStream != null) {
        log.info("Processing relocated web-fragment.xml file: {}", resourceName.toString());
        processWebFragmentXml(deployment, fragmentStream);
      } else {
        break;
      }

    }

  }

  protected void processWebXml(DeploymentInfo deployment, InputStream stream) {

    try {

      JAXBElement<?> webappElement = parseStream(stream);

      if (webappElement.getDeclaredType().equals(WebAppType.class)) {

        WebAppType webapp = (WebAppType) webappElement.getValue();
        processWebApp(deployment, webapp);

      }

    } catch (JAXBException | SAXException e) {
      log.warn("Failed to parse descriptor", e);
    }

  }

  private JAXBElement<?> parseStream(InputStream stream) throws JAXBException, SAXException {

    XMLReader reader = XMLReaderFactory.createXMLReader();

    NamespaceFilter filter = new NamespaceFilter();
    filter.setParent(reader);

    SAXSource source = new SAXSource(filter, new InputSource(stream));

    Unmarshaller unmarshaller = context.createUnmarshaller();
    return (JAXBElement<?>) unmarshaller.unmarshal(source);

  }

  protected void processWebFragmentXml(DeploymentInfo deployment, InputStream stream) {

    try {

      JAXBElement<?> webappElement = parseStream(stream);

      if (webappElement.getDeclaredType().equals(WebFragmentType.class)) {

        WebFragmentType webapp = (WebFragmentType) webappElement.getValue();
        processWebFragment(deployment, webapp);

      }

    } catch (JAXBException | SAXException e) {
      log.warn("Failed to parse descriptor", e);
    }

  }

  private void processWebApp(DeploymentInfo deployment, WebAppType webapp) {

    for (JAXBElement<?> jaxbElement : webapp.getModuleNameOrDescriptionAndDisplayName()) {
      processRootChildElement(deployment, jaxbElement);
    }

  }

  private void processWebFragment(DeploymentInfo deployment, WebFragmentType webapp) {

    for (JAXBElement<?> jaxbElement : webapp.getNameOrDescriptionAndDisplayName()) {
      processRootChildElement(deployment, jaxbElement);
    }

  }

  private void processRootChildElement(DeploymentInfo deployment, JAXBElement<?> jaxbElement) {
    // <context-param>
    if (jaxbElement.getDeclaredType().equals(ParamValueType.class)) {
      processContextParam(deployment, (ParamValueType) jaxbElement.getValue());
    }

    // <listener>
    if (jaxbElement.getDeclaredType().equals(ListenerType.class)) {
      processListener(deployment, (ListenerType) jaxbElement.getValue());
    }

    // <servlet>
    if (jaxbElement.getDeclaredType().equals(ServletType.class)) {
      processServlet(deployment, (ServletType) jaxbElement.getValue());
    }

    // <servlet-mapping>
    if (jaxbElement.getDeclaredType().equals(ServletMappingType.class)) {
      processServletMapping(deployment, (ServletMappingType) jaxbElement.getValue());
    }

    // <filter>
    if (jaxbElement.getDeclaredType().equals(FilterType.class)) {
      processFilter(deployment, (FilterType) jaxbElement.getValue());
    }

    // <filter-mapping>
    if (jaxbElement.getDeclaredType().equals(FilterMappingType.class)) {
      processFilterMapping(deployment, (FilterMappingType) jaxbElement.getValue());
    }
  }

  private void processFilterMapping(DeploymentInfo deployment, FilterMappingType value) {

    String filterName = value.getFilterName().getValue();

    List<javax.servlet.DispatcherType> dispatcherTypes = getDispatcherTypes(value.getDispatcher());

    for (Object object : value.getUrlPatternOrServletName()) {

      if (object instanceof UrlPatternType) {
        for (javax.servlet.DispatcherType dispatcherType : dispatcherTypes) {
          deployment.addFilterUrlMapping(
              filterName, ((UrlPatternType) object).getValue(), dispatcherType);
        }
      }

      if (object instanceof ServletNameType) {
        for (javax.servlet.DispatcherType dispatcherType : dispatcherTypes) {
          deployment.addFilterServletNameMapping(
              filterName, ((ServletNameType) object).getValue(), dispatcherType);
        }
      }

    }

  }

  private List<javax.servlet.DispatcherType> getDispatcherTypes(List<DispatcherType> dispatcher) {

    if (dispatcher == null || dispatcher.isEmpty()) {
      return Arrays.asList(javax.servlet.DispatcherType.REQUEST);
    }

    List<javax.servlet.DispatcherType> result = new ArrayList<>();
    for (DispatcherType dispatcherType : dispatcher) {
      result.add(javax.servlet.DispatcherType.valueOf(
          dispatcherType.getValue().trim().toUpperCase()));
    }
    return result;

  }

  private void processFilter(DeploymentInfo deployment, FilterType value) {

    String servletName = UUID.randomUUID().toString();
    if (value.getFilterName() != null && value.getFilterName().getValue() != null) {
      servletName = value.getFilterName().getValue().trim();
    }

    Class<Filter> filterClazz = loadClass(
        value.getFilterClass().getValue().trim(), Filter.class);
    FilterInfo filterInfo = new FilterInfo(servletName, filterClazz);

    filterInfo.setAsyncSupported(
        value.getAsyncSupported() != null && value.getAsyncSupported().isValue());

    if (value.getInitParam() != null) {
      for (ParamValueType param : value.getInitParam()) {
        if (param.getParamName() != null && param.getParamValue() != null) {
          filterInfo.addInitParam(
              param.getParamName().getValue(), param.getParamValue().getValue());
        }
      }
    }

    deployment.addFilter(filterInfo);

  }

  private void processServletMapping(DeploymentInfo deployment, ServletMappingType value) {

    if (value.getServletName() != null && value.getServletName().getValue() != null) {

      String servletName = value.getServletName().getValue();

      ServletInfo servletInfo = deployment.getServlets().get(servletName);
      if (servletInfo != null) {
        if (value.getUrlPattern() != null) {
          for (UrlPatternType urlPatternType : value.getUrlPattern()) {
            servletInfo.addMapping(urlPatternType.getValue().trim());
          }
        }
      }

    }

  }

  private void processServlet(DeploymentInfo deployment, ServletType value) {

    String servletName = UUID.randomUUID().toString();
    if (value.getServletName() != null && value.getServletName().getValue() != null) {
      servletName = value.getServletName().getValue().trim();
    }

    Class<Servlet> servletClazz = loadClass(
        value.getServletClass().getValue().trim(), Servlet.class);
    ServletInfo servletInfo = new ServletInfo(servletName, servletClazz);

    servletInfo.setAsyncSupported(
        value.getAsyncSupported() != null && value.getAsyncSupported().isValue());

    if (value.getLoadOnStartup() != null) {
      servletInfo.setLoadOnStartup(Integer.valueOf(value.getLoadOnStartup()));
    }

    if (value.getInitParam() != null) {
      for (ParamValueType param : value.getInitParam()) {
        if (param.getParamName() != null && param.getParamValue() != null) {
          servletInfo.addInitParam(
              param.getParamName().getValue(), param.getParamValue().getValue());
        }
      }
    }

    deployment.addServlet(servletInfo);

  }

  private void processListener(DeploymentInfo deployment, ListenerType value) {
    deployment.addListener(
        Servlets.listener(
            loadClass(value.getListenerClass().getValue(), EventListener.class)));
  }

  @SuppressWarnings("unchecked")
  private <T> Class<T> loadClass(String name, Class<T> expectedType) {
    try {
      return (Class<T>) classLoader.loadClass(name);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Cannot load class: " + name);
    }
  }

  private void processContextParam(DeploymentInfo deployment, ParamValueType param) {
    deployment.addInitParameter(
        param.getParamName().getValue(),
        param.getParamValue().getValue());
  }

}
