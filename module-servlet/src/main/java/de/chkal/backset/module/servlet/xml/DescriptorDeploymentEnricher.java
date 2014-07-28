package de.chkal.backset.module.servlet.xml;

import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.ServletInfo;

import java.io.InputStream;
import java.util.EventListener;
import java.util.UUID;

import javax.servlet.Servlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.chkal.backset.module.api.DeploymentEnricher;
import de.chkal.backset.module.servlet.xml.types.ListenerType;
import de.chkal.backset.module.servlet.xml.types.ParamValueType;
import de.chkal.backset.module.servlet.xml.types.ServletMappingType;
import de.chkal.backset.module.servlet.xml.types.ServletType;
import de.chkal.backset.module.servlet.xml.types.UrlPatternType;
import de.chkal.backset.module.servlet.xml.types.WebAppType;

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
  public void enrich(DeploymentInfo deployment) {

    InputStream webXmlStream = classLoader.getResourceAsStream("webapp/WEB-INF/web.xml");
    if (webXmlStream != null) {
      processWebXml(deployment, webXmlStream);
    }

  }

  protected void processWebXml(DeploymentInfo deployment, InputStream stream) {

    try {

      Unmarshaller unmarshaller = context.createUnmarshaller();
      JAXBElement<?> webappElement = (JAXBElement<?>) unmarshaller.unmarshal(stream);

      if (webappElement.getDeclaredType().equals(WebAppType.class)) {

        WebAppType webapp = (WebAppType) webappElement.getValue();
        processWebApp(deployment, webapp);

      }

    } catch (JAXBException e) {
      log.warn("Failed to parse descriptor", e);
    }

  }

  private void processWebApp(DeploymentInfo deployment, WebAppType webapp) {

    for (JAXBElement<?> jaxbElement : webapp.getModuleNameOrDescriptionAndDisplayName()) {

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

    }

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
